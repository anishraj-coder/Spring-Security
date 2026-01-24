import {Outlet} from "react-router";
import MainLayout from "@/layout/MainLayout.tsx";
import {useEffect, useState} from "react";
import {useAuthStore} from "@/store/useAuthStore.ts";
import {useShallow} from "zustand/react/shallow";
import {api} from "@/api/axiosConfig.ts";
import type {LoginResponse, User} from "@/utils/constants.ts";

const App=()=>{
    const [, setRehydrating] = useState<boolean>(true);
    const {logout,setUser,accessToken,setAccessToken}=useAuthStore(useShallow(state=>
        ({setAccessToken:state.setAccessToken,logout:state.logout,setUser:state.setUser,
            accessToken:state.accessToken})))
    useEffect(() => {
        const initialize=async()=>{
            try{
                const res=await api.post<LoginResponse>('/auth/refresh');
                setAccessToken(res.data.jwt);
                const resUser=await api.get<User>("/user/info");
                setUser(resUser.data);
            }catch (error) {
                console.log(error);
                logout();
            }finally {
                setRehydrating(false);
            }
        };
        if(!accessToken) initialize();
    }, [setUser,logout,setAccessToken,accessToken]);
    // if (isRehydrating) {
    //     return (
    //         <div className="h-screen w-full flex items-center justify-center dark bg-background">
    //             <Spinner className="h-8 w-8 text-primary" />
    //         </div>
    //     );
    // }
    return(
        <div className={`dark min-h-screen w-full bg-background text-foreground antialiased`}>
            <MainLayout>
                <Outlet/>
            </MainLayout>
        </div>
    );
}
export default App;