import {LoginForm} from "@/components/login/LoginForm.tsx";

const Login=()=>{
    return(<div className={`dark w-full h-screen`}>
        <div className="flex min-h-svh w-full items-center justify-center p-6 md:p-10">
            <div className="w-full max-w-sm">
                <LoginForm />
            </div>
        </div>
    </div>);
};
export  default Login;