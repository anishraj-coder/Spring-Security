import {useNavigate} from "react-router";
import {useMutation} from "@tanstack/react-query";
import type {ApiError, RegisterRequest, RegisterResponse} from "@/utils/constants.ts";
import {api} from "@/api/axiosConfig.ts";
import { toast } from "sonner";

export const useSignup=()=>{
    const navigate=useNavigate();
    return useMutation({
        mutationFn: async (request:RegisterRequest)=>{
            const res=await api.post<RegisterResponse>('/auth/register',request);
            return res.data;
        },
        onSuccess:()=>{
            toast.success("Account created!", {
                description: "Please check your email to verify your account before logging in.",
            });
            navigate("/login");
        },
        onError:(error: any)=>{
            const errorData=error.response?.data as ApiError;
            toast.error("Registration failed!!",{
                description: errorData.message
            })
        }
    });
}