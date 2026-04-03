import { AuthProvider } from "./context/AuthContext";
import Navbar from "./components/Navbar";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import MedicineCatalogPage from "./pages/MedicineCatalogPage";
import CartPage from "./pages/CartPage";
import OrderHistoryPage from "./pages/OrderHistoryPage";
import PharmacistDashboard from "./pages/PharmacistDashboard";
import AdminDashboard from "./pages/AdminDashboard";
import ProfilePage from "./pages/ProfilePage";

function Router() {
  const path = window.location.pathname;
  const routes = {
    "/":                          <MedicineCatalogPage />,
    "/medicines":                 <MedicineCatalogPage />,
    "/login":                     <LoginPage />,
    "/register":                  <RegisterPage />,
    "/cart":                      <CartPage />,
    "/orders":                    <OrderHistoryPage />,
    "/pharmacist/prescriptions":  <PharmacistDashboard />,
    "/admin/medicines":           <AdminDashboard />,
    "/profile":                   <ProfilePage />,
  };
  return routes[path] ?? (
      <div className="page-container" style={{ textAlign: "center" }}>
        <h2>404 — Page Not Found</h2>
        <a href="/" className="btn btn-primary">Go Home</a>
      </div>
  );
}

export default function App() {
  return (
      <AuthProvider>
        <Navbar />
        <main>
          <Router />
        </main>
      </AuthProvider>
  );
}