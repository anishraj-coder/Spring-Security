import {useMutation} from "@tanstack/react-query";
import {api} from "@/api/axiosConfig.ts";
import {toast} from "sonner";
import type {ApiError} from "@/utils/constants.ts";
import {useNavigate} from "react-router";

export const useForgetPassword=()=>{
    return useMutation(({
        mutationFn: async(email:string)=>{
            const res = await api.post('/auth/forget-password', { email });
            return res.data;
        },
        onSuccess: () => {
            toast.success("Reset link sent!", {
                description: "If an account exists, you will receive an email shortly.",
            });
        },
        onError: (error: any) => {
            const errorData = error.response?.data as ApiError;
            toast.error(errorData?.message || "Something went wrong");
        },
    }));
};

export const useResetPassword=()=>{
    const navigate=useNavigate();
    return useMutation(({
        mutationFn: async({token, password}:{token:string,password:string})=>{
            const res = await api.post('/auth/reset-password', { token, password });
            return res.data;
        },
        onSuccess: () => {
            toast.success("Password reset successful", {
                description: "Login again",
            });
            navigate("/login");
        },
        onError: (error: any) => {
            const errorData = error.response?.data as ApiError;
            toast.error(errorData?.message || "Something went wrong");
        },
    }));
}