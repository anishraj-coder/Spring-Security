import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Navigate, NavLink, useSearchParams} from "react-router";
import * as z from "zod";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {useResetPassword} from "@/hooks/useForget.ts";
import {Spinner} from "@/components/ui/spinner.tsx";
const resetSchema=z.object({
    password: z.string().min(5,"The minimum length of password should be 5")
        .max(15,"The max length of password is 15"),
    confirmPassword: z.string(),
}).refine(data=>data.password===data.confirmPassword
    ,{message:"The confirm password does not match the password",path:['confirmPassword']})

const ResetPassword=()=>{
    type ResetSchemaType=z.infer<typeof resetSchema>;
    const {formState:{errors},handleSubmit,register}=useForm<ResetSchemaType>({
        resolver: zodResolver(resetSchema),
        defaultValues:{password: ""}
    });
    const [params]=useSearchParams();
    const token=params.get("token");
    const {mutate: resetPassword,isPending}=useResetPassword();
    const onSubmit=(data:ResetSchemaType)=>{
        resetPassword({token: token!,password:data.password});
    }
    if(!token){
        return <Navigate to={'/home'} replace/>;
    }
    return (
        <div className="flex min-h-svh w-full items-center justify-center p-6 md:p-10 bg-background text-foreground">
            <Card className="w-full max-w-sm border-border bg-card">
                <CardHeader className="space-y-1">
                    <CardTitle className="text-2xl font-bold">Reset Password</CardTitle>
                    <CardDescription className="text-muted-foreground">
                        Enter your new Password
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                        <div className="space-y-2">
                            <label
                                htmlFor="password"
                                className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                            >
                                Password
                            </label>
                            <Input
                                id="password"
                                type="password"
                                placeholder="New password"
                                className={errors.password ? "border-destructive focus-visible:ring-destructive" : ""}
                                {...register("password")}
                            />
                            {errors.password && (
                                <p className="text-[0.8rem] font-medium text-destructive">
                                    {errors.password.message}
                                </p>
                            )}
                            <label
                                htmlFor="confirmPassword"
                                className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                            >
                                Confirm Password
                            </label>

                            <Input
                                id="confirmPassword"
                                type="password"
                                placeholder="Confirm new password"
                                className={errors.password ? "border-destructive focus-visible:ring-destructive" : ""}
                                {...register("confirmPassword")}
                            />
                            {errors.confirmPassword && (
                                <p className="text-[0.8rem] font-medium text-destructive">
                                    {errors.confirmPassword.message}
                                </p>
                            )}
                        </div>

                        <Button
                            className="w-full flex items-center justify-center gap-2"
                            type="submit"
                            disabled={isPending}
                        >
                            {isPending && (
                                <Spinner/>
                            )}
                            {isPending ? "Resetting....." : "Reset Password"}
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
export default ResetPassword;