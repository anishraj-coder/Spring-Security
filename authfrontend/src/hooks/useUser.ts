import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { api } from "@/api/axiosConfig";
import { useAuthStore } from "@/store/useAuthStore";
import { toast } from "sonner";
import type { User, ApiError } from "@/utils/constants";

export const useUserProfile = () => {
    const queryClient = useQueryClient();
    const setUser = useAuthStore((state) => state.setUser);
    const currentUser = useAuthStore((state) => state.user);

    const profile = useQuery({
        queryKey: ["user", "info"],
        queryFn: async () => {
            const res = await api.get<User>("/user/info");
            return res.data;
        },
    });

    const updateProfile = useMutation({
        mutationFn: async (data: Partial<User>) => {
            // Backend requirement: PATCH /api/user?user_id=UUID
            const res = await api.patch<User>("/user", data, {
                params: { user_id: currentUser?.id },
            });
            return res.data;
        },
        onSuccess: (updatedUser) => {
            // Update both the cache and the global auth store
            queryClient.setQueryData(["user", "info"], updatedUser);
            setUser(updatedUser);
            toast.success("Profile updated successfully");
        },
        onError: (error: any) => {
            const errorData = error.response?.data as ApiError;
            toast.error(errorData?.message || "Update failed");
        },
    });

    return { profile, updateProfile };
};