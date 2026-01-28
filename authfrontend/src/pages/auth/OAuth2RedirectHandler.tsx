import {useNavigate, useSearchParams} from "react-router";
import {useAuthStore} from "@/store/useAuthStore.ts";
import {useShallow} from "zustand/react/shallow";
import {useEffect} from "react";
import {api} from "@/api/axiosConfig.ts";
import type {User} from "@/utils/constants.ts";
import {toast} from "sonner";
import {Spinner} from "@/components/ui/spinner.tsx";

const OAuth2RedirectHandler=()=>{

    const [searchParams]=useSearchParams();
    const navigate=useNavigate();
    const{setUser,setAccessToken}=useAuthStore(useShallow(state=>({setUser:state.setUser,setAccessToken:state.setAccessToken})));
    useEffect(()=>{
        const token=searchParams.get("token");
        const error = searchParams.get('error');
        if(token){
            setAccessToken(token);
            api.get<User>("/user/info")
                .then(res=>{
                    setUser(res.data);
                    toast.success("Login successful!", {
                        description: `Welcome, ${res.data.name}`
                    });
                    navigate("/");
                }).catch((err) => {
                console.error("OAuth2 Error:", err);
                toast.error("Authentication Failed", {
                    description: "Could not retrieve user profile."
                });
                navigate("/login");
            });
        }else{
            toast.error("Authentication failed!!!",{description: error})
            navigate('/login', { state: { error: error || 'Authentication failed' } });
        }
    },[navigate, searchParams, setAccessToken, setUser]);

    return (
        <div className="flex h-screen w-full items-center justify-center">
            <div className="flex flex-col items-center gap-4">
                <Spinner className="h-8 w-8 text-primary" />
                <p className="text-muted-foreground animate-pulse">Completing authentication...</p>
            </div>
        </div>
    );
};

export default OAuth2RedirectHandler;