import React from 'react';
import Header from "@/components/home/Header.tsx";
import {Toaster} from '@/components/ui/sonner';
import {useLocation} from "react-router";


type MainLayoutProps={
    children: React.ReactNode
}

const MainLayout=({children}:MainLayoutProps)=>{
    const hidePath=['/login','/signup'];
    const location=useLocation();
    const isHidden=hidePath.includes(location.pathname);
    return (
        <main className={`w-full `}>
            {!isHidden&&<Header/>}
            {children}
            <Toaster richColors={true} position={'top-right'}/>
        </main>
    );
};
export default  MainLayout;