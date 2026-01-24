import React from "react";
import {useAuthStore} from "@/store/useAuthStore.ts";
import {Navigate, useLocation} from "react-router";


interface RoleGuardProps{
    children: React.ReactNode;
    allowedRoles: string[];
}

const RoleGuard=({children,allowedRoles}:RoleGuardProps)=>{

    const user=useAuthStore(state=>state.user);
    const isAllowed=user?.roles.some(role=>allowedRoles.includes(role));
    const location=useLocation();
    if(!user){
        return <Navigate to={'/login'} state={location} replace/>;
    }
    if(!isAllowed){
        return <Navigate to={'/'} replace/>
    }

    return <>{children}</>;
};
export default RoleGuard;