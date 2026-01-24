import {keepPreviousData, useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {api} from "@/api/axiosConfig.ts";
import type {PaginatedResponse, User} from "@/utils/constants.ts";
import {toast} from "sonner";

export  const useGetAllUsers=(page:number=1,size:number=5
                              ,sort:string='name,asc')=>{
    return useQuery(({
        queryKey: ['admin','users',page,size],
        queryFn: async ()=>{
            const res=await api.get<PaginatedResponse<User>>('/user'
                ,{params: {size,page,sort}});
            return res.data;
        },
        placeholderData: keepPreviousData
    }));
}
export const useAdminActions=()=>{
    const queryClient=useQueryClient();
    const verifyUser=useMutation({
        mutationFn:async(userId:string)=>{
            const res= await api.patch<User>('/user/verify',null,
                {params: {user_id:userId}});
            return res.data;
        },
        onSuccess: async (user:User)=>{
            await queryClient.invalidateQueries({
                queryKey: ['admin', 'users']
            });

            toast.success(`User ${user?.name} is now verified`);
        }
    });
    const deleteUser=useMutation({
        mutationFn:async(userId:string)=>{
            const res=await api.delete('/user',{params:{user_id:userId}});
            return res.data;
        },
        onSuccess:async ()=>{
            await queryClient.invalidateQueries({
                queryKey: ['admin', 'users']
            });
            toast.success("The user has now been deleted");
        }
    });
    return {verifyUser,deleteUser};
}