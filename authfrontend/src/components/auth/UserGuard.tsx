import RoleGuard from "@/components/auth/RoleGuard.tsx";
import React from "react";

interface UserGuardProps{
    children: React.ReactNode;
}

const UserGuard=({children}:UserGuardProps)=>{
    return (
        <RoleGuard allowedRoles={['USER','ADMIN']}>
            {children}
        </RoleGuard>
    );
};
export default UserGuard;