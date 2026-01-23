import {useAuthStore} from "@/store/useAuthStore.ts";
import {useNavigate} from "react-router";
import {useMutation} from "@tanstack/react-query";
import type {LoginRequest, LoginResponse, User} from "@/utils/constants.ts";
import {api} from "@/api/axiosConfig.ts";
import {toast} from "sonner";

export const useLogin=()=>{
    const setAccessToken=useAuthStore.getState().setAccessToken;
    const setUser=useAuthStore.getState().setUser;
    const navigate=useNavigate();
    return useMutation({
        mutationFn: async(credentials: LoginRequest)=>{
            const res=await
                api.post<LoginResponse>('/auth/login',credentials);
            const {jwt}=res.data;
            setAccessToken(jwt);
            const userRes=await api.get<User>('/user/info');
            const user=userRes.data;
            setUser(user);
            return {jwt,user};

        },
        onSuccess:(data)=>{
            toast.success("Login successfully",{
                description: `Welcome back!, ${data.user.name}`
            })
            navigate("/");
        },
        onError:(error)=>{
            console.log(error);
        }
    });
}
