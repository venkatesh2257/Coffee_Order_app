from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List
import base64

app = FastAPI()

class CoffeeOrder(BaseModel):
    user_id: str
    coffee_id: str
    quantity: int
    image_base64: str  # Image stored as base64

# Mock database
orders = []

@app.post("/order")
async def place_order(order: CoffeeOrder):
    # Process image (decoding example)
    try:
        image_data = base64.b64decode(order.image_base64)
        # Here you could save the image to storage
    except Exception as e:
        raise HTTPException(status_code=400, detail="Invalid image encoding")

    orders.append(order)
    return {"status": "success", "message": "Order placed successfully"}

@app.get("/orders/{user_id}", response_model=List[CoffeeOrder])
async def get_orders(user_id: str):
    user_orders = [o for o in orders if o.user_id == user_id]
    return user_orders

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
