import {useAuthStore} from "@/store/useAuthStore.ts";
import {useMutation} from "@tanstack/react-query";
import {api} from '@/api/axiosConfig.ts';
import {toast} from "sonner";

export const useLogout=()=>{
    const logout=useAuthStore.getState().logout;
    const user=useAuthStore.getState().user;
    return useMutation({
        mutationFn: async()=>{
            if(!user)return;
            await api.post("/auth/logout");
            logout();
        },
        onError:(error)=>{
            toast.error("Logout failed",{
                description: error.message
            })
        },
        onSuccess:()=>{
            toast.success("Logout successful");
        },

    })
}