import {QueryClient} from "@tanstack/react-query";
export const queryClient=new QueryClient({
    defaultOptions:{
        queries:{
            retry: 1,
            gcTime: 5*60,
            refetchOnWindowFocus: true,
        },
        mutations:{
            retry: 2,
            gcTime: 4*60,
        }
    }
});