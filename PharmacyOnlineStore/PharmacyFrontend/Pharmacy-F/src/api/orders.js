import API from "./axiosConfig";

export const placeOrder = (data) => API.post("/orders", data);
export const getOrderHistory = () => API.get("/orders/history");
export const getAllOrders = () => API.get("/orders");