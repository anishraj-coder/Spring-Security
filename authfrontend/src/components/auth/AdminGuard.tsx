import RoleGuard from "@/components/auth/RoleGuard.tsx";
import React from "react";

interface AdminGuardProps{
    children:React.ReactNode;
}

const AdminGuard=({children}:AdminGuardProps)=>{
    return (
        <RoleGuard allowedRoles={['ADMIN']}>
            {children}
        </RoleGuard>
    );
};
export default AdminGuard;