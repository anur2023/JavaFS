import API from "./axiosConfig";

export const getAllMedicines = (categoryId) =>
    API.get("/medicines", { params: categoryId ? { categoryId } : {} });

export const addMedicine = (data) => API.post("/medicines", data);
export const updateMedicine = (id, data) => API.put(`/medicines/${id}`, data);
export const deleteMedicine = (id) => API.delete(`/medicines/${id}`);