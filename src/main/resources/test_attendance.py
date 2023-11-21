import requests
import asyncio
import time
import json
from websockets.client import connect

async def request() -> str:
    session: requests.Session = requests.Session()
    headers: dict = {"Authorization": "Basic cHJpbmNpcGFsOjEyMzQ="}
    data: str = session.get("http://roundhouse.proxy.rlwy.net:42552/api/v1/rfid/credentials", headers=headers).json()
    return data

async def main():
    print("Hello")
    credentials: list[dict] = await request()
    ws = await connect("ws://esp32:1234@roundhouse.proxy.rlwy.net:42552/websocket/scanner")
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
