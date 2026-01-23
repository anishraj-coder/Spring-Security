import { StrictMode } from "react"
import { createRoot } from "react-dom/client"

import "./index.css"
import {RouterProvider} from "react-router";
import {routeConfig} from "@/routes/routeConfig.tsx";
import {QueryClientProvider} from "@tanstack/react-query";
import {queryClient} from "@/api/queryClient.ts";
import {ReactQueryDevtools} from "@tanstack/react-query-devtools";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
        <ReactQueryDevtools initialIsOpen={false} />
        <RouterProvider router={routeConfig}/>
    </QueryClientProvider>
  </StrictMode>
)
