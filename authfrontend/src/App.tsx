import {Outlet} from "react-router";
import MainLayout from "@/layout/MainLayout.tsx";

const App=()=>{

    return(
        <div className={`dark min-h-screen w-full bg-background text-foreground antialiased`}>

            <MainLayout>
                <Outlet/>
            </MainLayout>
        </div>
    );
}
export default App;