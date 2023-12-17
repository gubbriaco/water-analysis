import asyncio
from bleak import BleakScanner

async def main():
    devices = await BleakScanner.discover()
    for d in devices:
        print(f"device_name:{d.name}, address:{d.address}, RSSI:{d.rssi}, advertisement_data:{d.details}")

asyncio.run(main())
