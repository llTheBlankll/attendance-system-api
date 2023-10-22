import hashlib
import psycopg2

def hashMD5(value: str):
    return hashlib.md5(repr(value).encode()).hexdigest()


def connect():
    """ Connect to the PostgreSQL database server """
    conn = None
    try:
        # read connection parameters

        # connect to the PostgreSQL server
        print('Connecting to the PostgreSQL database...')
        conn = psycopg2.connect(
            "dbname='attendance_system' user='username' host='localhost' password='password'")

        # create a cursor
        cur = conn.cursor()

        # execute a statement
        print('PostgreSQL database version:')
        cur.execute("SELECT lrn FROM students")

        # display the PostgreSQL database server version
        students_lrn = cur.fetchall()
        for lrn in students_lrn:
            lrn = lrn[0]
            hashed_lrn = hashMD5(lrn)
            cur.execute("INSERT INTO scan(lrn, hashed_lrn) VALUES(%s, %s)", (lrn, hashed_lrn))
        
        conn.commit()
        cur.close()
        conn.close()
            

        # close the communication with the PostgreSQL
        cur.close()
    except (Exception, psycopg2.DatabaseError) as error:
        print(error)
    finally:
        if conn is not None:
            conn.close()
            print('Database connection closed.')

if __name__ == '__main__':
    connect()
