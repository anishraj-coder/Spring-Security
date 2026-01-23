import {createBrowserRouter} from "react-router";
import App from "@/App.tsx";
import Home from "@/pages/Home.tsx";
import Login from "@/pages/Login.tsx";
import SignUp from "@/pages/SignUp.tsx";
import OAuth2RedirectHandler from "@/pages/auth/OAuth2RedirectHandler.tsx";

export const routeConfig=createBrowserRouter([{
    path:'/',
    element:<App/>,
    children:[
        {index:true,element:<Home/>},
        {path:"/login",element:<Login/>},
        {path:"/signup",element:<SignUp/>},
        {path: '/oauth2/redirect',element:<OAuth2RedirectHandler/>}
    ]
}])