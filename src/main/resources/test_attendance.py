import requests
import asyncio
import time
import json
from websockets.client import connect

async def request() -> str:
    session: requests.Session = requests.Session()
    headers: dict = {"Authorization": "Bearer cHJpbmNpcGFsOjEyMzQ="}
    data: str = session.get("http://localhost:8080/api/rfid/all", headers=headers).json()
    return data

async def main():
    print("Hello")
    credentials: list[dict] = await request()
    ws = await connect("ws://esp32:1234@localhost:8080/websocket/scanner")
    for credential in credentials:
        data: dict = {
            "mode": "out",
            "hashedLrn": credential.get("hashedLrn")
        }

        data_send = json.dumps(data)
        await ws.send(str(data_send))
        data_received = json.loads(await ws.recv())
        print(f"Student {credential.get('hashedLrn')} {data_received.get('message')}")
    await ws.close()
        

if __name__ == "__main__":
    asyncio.run(main())
