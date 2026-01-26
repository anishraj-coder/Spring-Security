import React, {useEffect} from 'react';
import Header from "@/components/home/Header.tsx";
import {Toaster} from '@/components/ui/sonner';
import {useLocation} from "react-router";
import {SidebarProvider} from "@/components/ui/sidebar.tsx";
import AppSidebar from "@/components/home/AppSidebar.tsx";


type MainLayoutProps={
    children: React.ReactNode
}

const MainLayout=({children}:MainLayoutProps)=>{
    const hidePath=['/login','/signup'];
    const location=useLocation();
    const isHidden=hidePath.includes(location.pathname);
    useEffect(() => {
        document.documentElement.classList.add("dark");
    }, []);
    return (
        <SidebarProvider defaultOpen={false} >
            <AppSidebar />
            <div className={`flex w-full min-h-screen`}>
                <main className={`w-full `}>
                    {(!isHidden)&&<Header/>}
                    {children}
                    <Toaster richColors={true} position={'top-right'}/>
                </main>
            </div>
        </SidebarProvider>
    );
};
export default  MainLayout;