import type {User} from "@/utils/constants.ts";
import {create} from 'zustand';
import {createJSONStorage, devtools, persist} from "zustand/middleware";
export interface AuthState{
    accessToken:string|null;
    user: User|null;
    setAccessToken: (accessToken:string|null)=>void;
    setUser: (user:User|null)=>void;
    logout: ()=>void;
}

export const useAuthStore=create<AuthState>()(
    devtools(persist(set=>({
        accessToken: null,
        user: null,
        setAccessToken: (token:string|null)=>set({accessToken: token}),
        setUser: (givenUser:User|null)=>set({user:givenUser}),
        logout: ()=>set({user:null,accessToken:null})
    }),
        {
            name:'auth-storage',
            storage: createJSONStorage(()=>sessionStorage),
            partialize:(state)=>({user:state.user}),
        }))
);