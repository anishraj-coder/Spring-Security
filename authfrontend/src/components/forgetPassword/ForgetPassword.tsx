import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { useForgetPassword } from "@/hooks/useForget"; // Assuming this file exists based on previous generation
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { NavLink } from "react-router";

/**
 * Since the previous build failed due to missing local UI components like 'Field' and 'Spinner',
 * I have refactored this file to use standard Tailwind and Lucide icons for maximum compatibility
 * while maintaining your design aesthetic.
 */

const schema = z.object({
    email: z.email("Please enter a valid email"),
});

const ForgetPassword = () => {
    const { mutate, isPending } = useForgetPassword();

    const {
        register,
        handleSubmit,
        formState: { errors }
    } = useForm({
        resolver: zodResolver(schema),
        defaultValues: {
            email: ""
        }
    });

    const onSubmit = (data: { email: string }) => {
        mutate(data.email);
    };

    return (
        <div className="flex min-h-svh w-full items-center justify-center p-6 md:p-10 bg-background text-foreground">
            <Card className="w-full max-w-sm border-border bg-card">
                <CardHeader className="space-y-1">
                    <CardTitle className="text-2xl font-bold">Forgot Password</CardTitle>
                    <CardDescription className="text-muted-foreground">
                        Enter your email to receive a password reset link.
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                        <div className="space-y-2">
                            <label
                                htmlFor="email"
                                className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                            >
                                Email Address
                            </label>
                            <Input
                                id="email"
                                type="email"
                                placeholder="name@example.com"
                                className={errors.email ? "border-destructive focus-visible:ring-destructive" : ""}
                                {...register("email")}
                            />
                            {errors.email && (
                                <p className="text-[0.8rem] font-medium text-destructive">
                                    {errors.email.message}
                                </p>
                            )}
                        </div>

                        <Button
                            className="w-full flex items-center justify-center gap-2"
                            type="submit"
                            disabled={isPending}
                        >
                            {isPending && (
                                <svg
                                    className="animate-spin h-4 w-4 text-current"
                                    xmlns="http://www.w3.org/2000/svg"
                                    fill="none"
                                    viewBox="0 0 24 24"
                                >
                                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                </svg>
                            )}
                            {isPending ? "Sending..." : "Send Reset Link"}
                        </Button>

                        <div className="text-center text-sm">
                            <span className="text-muted-foreground">Remember your password? </span>
                            <NavLink
                                to="/login"
                                className="font-medium text-primary underline underline-offset-4 hover:text-primary/80 transition-colors"
                            >
                                Login
                            </NavLink>
                        </div>
                    </form>
                </CardContent>
            </Card>
        </div>
    );
};

export default ForgetPassword;