import {useAuthStore} from "@/store/useAuthStore.ts";
import {useNavigate} from "react-router";
import {useMutation, useQuery} from "@tanstack/react-query";
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
            const {email,jwt}=res.data;
            setAccessToken(res.data.jwt);
            const userRes=await api.get<User>('/user/single',
                {params:{email:email}});
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

export const useGetUser=(email:string)=>{
    return useQuery({
        queryKey: [`user_${email}`],
        queryFn: async()=>{
            const res=await api.get<User>('/user/single',
                {params: {email:email}});
            return res.data;
        },
        gcTime: 10*60,
        staleTime: 30*60,
    })
}