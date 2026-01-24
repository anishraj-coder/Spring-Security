import { Button } from "@/components/ui/button";
import { NavLink } from "react-router";

const NotFound = () => {
    return (
        <div className="flex h-[80vh] w-full flex-col items-center justify-center text-center px-4">
            <h1 className="text-9xl font-extrabold tracking-widest text-primary">404</h1>
            <div className="bg-destructive px-2 text-sm rounded rotate-12 absolute">
                Page Not Found
            </div>
            <p className="text-muted-foreground mt-5 mb-8">
                The page you are looking for doesn't exist or has been moved.
            </p>
            <NavLink to="/">
                <Button size="lg">Return Home</Button>
            </NavLink>
        </div>
    );
};

export default NotFound;