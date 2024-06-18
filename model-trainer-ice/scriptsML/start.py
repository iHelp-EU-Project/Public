
import uvicorn

if __name__ == "__main__":
    uvicorn.run("initServerFastApi:app", host="192.168.1.139", port=8001, reload=True)