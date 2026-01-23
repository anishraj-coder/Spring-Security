import {Outlet, useLocation} from "react-router";
import Header from "@/components/home/Header.tsx";
import {Toaster} from '@/components/ui/sonner';

const App=()=>{
    const hidePath=['/login','/signup'];
    const location=useLocation();
    const isHidden=hidePath.includes(location.pathname);
    return(
        <div className={`dark min-h-screen w-full bg-background text-foreground antialiased`}>
            {!isHidden&&<Header/>}
            <main>
                <Outlet/>
            </main>
            <Toaster position={`top-right`} richColors={true}/>
        </div>
    );
}
export default App;