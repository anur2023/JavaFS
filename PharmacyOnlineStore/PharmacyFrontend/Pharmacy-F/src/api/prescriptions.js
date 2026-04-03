import API from "./axiosConfig";

export const getPendingPrescriptions = () =>
    API.get("/prescriptions/pending");

export const validatePrescription = (id, status, reason) =>
    API.post(`/prescriptions/${id}/validate`, { status, reason });