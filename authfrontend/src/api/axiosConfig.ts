import axios from "axios";
import {useAuthStore} from "@/store/useAuthStore.ts";
import {baseUrl, type LoginResponse} from "@/utils/constants.ts";


export const api=axios.create({
    baseURL: baseUrl,
    withCredentials:true,
});

api.interceptors.request.use(config=>{
    const token= useAuthStore.getState().accessToken;
    if(token){
        config.headers.Authorization= `Bearer ${token}`;
    }
    return config;
});

api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const token=useAuthStore.getState().accessToken;
        const originalRequest=error.config;
        if(token&&error.response?.status===401&&!originalRequest._retry&&!originalRequest.url.includes('/auth/refresh')){
            originalRequest._retry=true;
            try{
                const res=await axios.
                post<LoginResponse>("http://localhost:8082/api/auth/refresh",{},
                    {withCredentials:true});
                const {jwt}=res.data;
                if(jwt)useAuthStore.getState().setAccessToken(jwt);
                else Promise.reject(error);
                originalRequest.headers.Authorization=`Bearer ${jwt}`;
                return api(originalRequest);
            }catch (refreshError){
                useAuthStore.getState().logout();
                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error);
    }
);