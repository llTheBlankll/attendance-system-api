package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.entities.Guardian;
import com.pshs.attendancesystem.entities.RfidCredentials;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.messages.SectionMessages;
import com.pshs.attendancesystem.messages.StudentMessages;
import com.pshs.attendancesystem.repositories.RfidCredentialsRepository;
import com.pshs.attendancesystem.repositories.SectionRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import com.pshs.attendancesystem.security.PasswordGenerator;
import com.pshs.attendancesystem.services.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class StudentServiceImpl implements StudentService {
	private final StudentRepository studentRepository;
	private final RfidCredentialsRepository rfidCredentialsRepository;
	private final SectionRepository sectionService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public StudentServiceImpl(StudentRepository studentRepository, RfidCredentialsRepository rfidCredentialsRepository, SectionRepository sectionService) {
		this.studentRepository = studentRepository;
		this.rfidCredentialsRepository = rfidCredentialsRepository;
		this.sectionService = sectionService;
	}

	private String hashMD5(String value) {
		try {
			// Create an instance of MessageDigest with MD5 algorithm
			MessageDigest md = MessageDigest.getInstance("MD5");

			// Convert the value to bytes
			byte[] valueBytes = value.getBytes();

			// Update the MessageDigest with the value bytes
			md.update(valueBytes);

			// Get the hash value as bytes
			byte[] hashBytes = md.digest();

			// Convert the hash bytes to a hexadecimal string
			StringBuilder sb = new StringBuilder();
			for (byte b : hashBytes) {
				sb.append(String.format("%02x", b));
			}

			// Return the hexadecimal string representation of the hash value
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// Handle the exception if MD5 algorithm is not available
			logger.error(e.getMessage());
		}

		return null;
	}

	/**
	 * Retrieves all students.
	 *
	 * @return an iterable of student objects
	 */
	public Iterable<Student> getAllStudent() {
		return this.studentRepository.findAll();
	}

	@Override
	public boolean isLrnExist(Long lrn) {
		return this.studentRepository.existsById(lrn);
	}

	/**
	 * Adds a new student to the database.
	 *
	 * @param student the student object to be added
	 * @return a message indicating the success of the operation
	 */
	@CachePut(value = "student", key = "#student.lrn")
	public String createStudent(Student student) {
		if (student.getLrn() == null) {
			return StudentMessages.STUDENT_INVALID_LRN;
		}

		if (this.studentRepository.existsById(student.getLrn())) {
			return StudentMessages.STUDENT_EXISTS;
		} else if (!this.sectionService.existsById(student.getStudentSection().getSectionId())) {
			return SectionMessages.SECTION_NOT_FOUND;
		}

		RfidCredentials studentRfidCredentials = new RfidCredentials();
		PasswordGenerator passwordGenerator = new PasswordGenerator();
		// First save the un-hashed student's learning resource number.

		// Then encode the student's learning resource number with MD5.
		String salt = passwordGenerator.generate(16);
		studentRfidCredentials.setLrn(student.getLrn());
		studentRfidCredentials.setHashedLrn(hashMD5(student.getLrn() + salt));
		studentRfidCredentials.setSalt(salt);

		// Set guardian student lrn
		Set<Guardian> guardianSet = student.getGuardian();
		for (Guardian guardian : guardianSet) {
			guardian.setStudent(student);
		}

		// Add the hashed lrn to the database.
		this.studentRepository.save(student);
		this.rfidCredentialsRepository.save(studentRfidCredentials);

		return StudentMessages.STUDENT_CREATED;
	}

	/**
	 * Deletes a student from the database.
	 *
	 * @param student the student object to be deleted
	 * @return a string indicating the result of the deletion
	 */
	@CacheEvict(value = "student", key = "#student.lrn")
	public String deleteStudent(@RequestBody Student student) {
		if (!this.studentRepository.existsById(student.getLrn())) {
			return StudentMessages.STUDENT_NOT_FOUND;
		}

		this.studentRepository.delete(student);
		return StudentMessages.STUDENT_DELETED;
	}

	/**
	 * Deletes a student by their ID.
	 *
	 * @param id the ID of the student to delete
	 * @return a message indicating if the student was deleted or if they do not exist
	 */
	@CacheEvict(value = "student", key = "#id")
	public String deleteStudentById(Long id) {
		if (!this.studentRepository.existsById(id)) {
			return StudentMessages.STUDENT_NOT_FOUND;
		}

		this.studentRepository.deleteById(id);
		return StudentMessages.STUDENT_DELETED;
	}

	// SEARCH FUNCTION

	/**
	 * Retrieves a list of students by grade level.
	 *
	 * @param gradeName the name of the grade level to search for
	 * @return an iterable collection of Student objects
	 */
	@Cacheable(value = "student", key = "#gradeName")
	public Iterable<Student> getStudentByGradeLevel(String gradeName) {
		if (!this.studentRepository.isGradeNameExist(gradeName)) {
			Stream<Student> empty = Stream.empty();
			return empty::iterator; // Return empty.
		}

		return this.studentRepository.getStudentByGradeName(gradeName);
	}

	@Override
	@Cacheable(value = "student", key = "#lrn")
	public boolean isStudentAttended(Long lrn, LocalDate date) {
		return studentRepository.isStudentAttended(lrn, LocalDate.now());
	}

	/**
	 * Retrieves a student by their unique learning resource number (LRN).
	 *
	 * @param lrn the learning resource number of the student
	 * @return the student with the specified LRN, or null if not found
	 */
	@Override
	@Cacheable(value = "student", key = "#lrn")
	public Student getStudentById(Long lrn) {
		if (!this.studentRepository.existsById(lrn)) {
			return new Student();
		}

		return this.studentRepository.findStudentByLrn(lrn);
	}

	/**
	 * Updates a student in the system.
	 *
	 * @param student the student object to be updated
	 * @return a string indicating the result of the update
	 */
	@Override
	@CachePut(value = "student", key = "#student.lrn")
	public String updateStudent(@RequestBody Student student) {
		if (!this.studentRepository.existsById(student.getLrn())) {
			return StudentMessages.STUDENT_NOT_FOUND;
		}

		this.studentRepository.save(student);
		return StudentMessages.STUDENT_UPDATED;
	}

	@Override
	@Cacheable(value = "student", key = "#sectionId")
	public Iterable<Student> getAllStudentWithSectionId(Integer sectionId) {
		return this.studentRepository.getStudentBySectionId(sectionId);
	}

	@Override
	@Cacheable(value = "student", key = "#sectionId")
	public long countStudentsBySectionId(Integer sectionId) {
		return this.studentRepository.countStudentsBySectionId(sectionId);
	}
}