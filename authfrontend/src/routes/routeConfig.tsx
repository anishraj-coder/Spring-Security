import {createBrowserRouter} from "react-router";
import App from "@/App.tsx";
import Home from "@/pages/Home.tsx";
import Login from "@/pages/Login.tsx";
import SignUp from "@/pages/SignUp.tsx";
import OAuth2RedirectHandler from "@/pages/auth/OAuth2RedirectHandler.tsx";
import UserGuard from "@/components/auth/UserGuard.tsx";
import UserDashboard from "@/components/user/UserDashboard.tsx";
import AdminGuard from "@/components/auth/AdminGuard.tsx";
import AdminDashboard from "@/components/admin/AdminDashboard.tsx";
import ForgetPassword from "@/components/forgetPassword/ForgetPassword.tsx";
import ResetPassword from "@/components/forgetPassword/ResetPassword.tsx";
import NotFound from "@/components/error/NotFound.tsx";

export const routeConfig = createBrowserRouter([{
    path: '/',
    element: <App />,
    children: [
        { index: true, element: <Home /> },
        { path: "/login", element: <Login /> },
        { path: "/signup", element: <SignUp /> },
        { path: '/oauth2/redirect', element: <OAuth2RedirectHandler /> },
        { path: '/forget-password',element:<ForgetPassword/>},
        { path: '/reset-password',element:<ResetPassword/>},
        {
            path: "/dashboard",
            element: (
                <UserGuard>
                    <UserDashboard />
                </UserGuard>
            )
        },
        {
            path: "/admin",
            element: (
                <AdminGuard>
                    <AdminDashboard />
                </AdminGuard>
            )
        },
        {path:'*',element:<NotFound/>}
    ]
}]);