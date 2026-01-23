import { cn } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"
import {
    Field,
    FieldDescription,
    FieldGroup,
    FieldLabel,
} from "@/components/ui/field"
import { Input } from "@/components/ui/input"
import {NavLink} from "react-router";
import {z} from 'zod';
import {useForm} from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {useLogin} from "@/api/useLogin.ts";
import {toast} from "sonner";
import {Spinner} from '@/components/ui/spinner';
import type {ApiError} from "@/utils/constants.ts";

const loginSchema=z.object({
    email: z.email("The email is invalid"),
    password: z.string()
        .min(5,{message: `The email must be at least 5 characters long`})
        .max(15,{message:"The email must be at most 15 character long"}),
});

export function LoginForm({
                              className,
                              ...props
                          }: React.ComponentProps<"div">) {

    const {mutate,isPending}=useLogin();
    type LoginSchemaType = z.infer<typeof loginSchema>;
    const {handleSubmit,register,formState: {errors}}=useForm<LoginSchemaType>({
        resolver: zodResolver(loginSchema),
        defaultValues: {email: "",password: ""}
    });
    const onSubmit=(values:LoginSchemaType)=>{
        mutate(values,{
            onError: (error:any)=>{
                const errorData=error.response?.data as ApiError;
                const errorMessage = errorData?.message || "An unexpected error occurred";

                if (errorData?.statusCode === 401 && errorMessage.includes("disabled")) {
                    toast.error("Account Disabled", {
                        description: "Please check your email to verify your account.",
                    });
                } else {
                    toast.error(errorMessage);
                }
            },
        })
    }
    return (
        <div className={cn("flex flex-col gap-6", className)} {...props}>
            <Card>
                <CardHeader>
                    <CardTitle>Login to your account</CardTitle>
                    <CardDescription>
                        Enter your email below to login to your account
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <form onSubmit={handleSubmit(onSubmit)}>
                        <FieldGroup>
                            <Field>
                                <FieldLabel htmlFor="email">Email</FieldLabel>
                                <Input
                                    id="email"
                                    type="email"
                                    placeholder="m@example.com"
                                    required
                                    {...register('email')}
                                />
                                {errors.email && (
                                    <p className="text-sm font-medium text-destructive">{errors.email.message}</p>
                                )}
                            </Field>
                            <Field>
                                <div className="flex items-center">
                                    <FieldLabel htmlFor="password">Password</FieldLabel>
                                    <NavLink
                                        to="/forget-password"
                                        className="ml-auto inline-block text-sm underline-offset-4 hover:underline"
                                    >
                                        Forgot your password?
                                    </NavLink>
                                </div>
                                <Input id="password" type="password"
                                       required {...register('password')}/>
                                {errors.password && (
                                    <p className="text-sm font-medium text-destructive">{errors.password.message}</p>
                                )}
                            </Field>
                            <Field>
                                <Button disabled={isPending} type="submit">
                                    {isPending?<Spinner/>:"Login"}
                                </Button>
                                <Button variant="outline" type="button">
                                    Login with Google
                                </Button>
                                <FieldDescription className="text-center">
                                    Don&apos;t have an account?
                                    <NavLink to={`/signup`} className={`ml-2`}>Sign Up</NavLink>
                                </FieldDescription>
                            </Field>
                        </FieldGroup>
                    </form>
                </CardContent>
            </Card>
        </div>
    )
}
