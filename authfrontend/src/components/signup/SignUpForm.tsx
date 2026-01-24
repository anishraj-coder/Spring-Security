import { Button } from "@/components/ui/button.tsx"
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card.tsx"
import {
    Field,
    FieldDescription,
    FieldGroup,
    FieldLabel,
} from "@/components/ui/field.tsx"
import { Input } from "@/components/ui/input.tsx"
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";
import {NavLink} from "react-router";
import * as z from 'zod';
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {useSignup} from "@/hooks/useSignup.ts";
import type {ApiError, RegisterRequest} from "@/utils/constants.ts";
import {toast} from "sonner";
import {Spinner} from "@/components/ui/spinner.tsx";

const signUpSchema=z.object({
    email: z.email({message: "Please enter a valid email"}),
    password: z.string().min(5,"Password must be at least 5 characters")
        .max(15, "Password cant be longer than 15 characters"),
    gender: z.enum(["MALE","FEMALE","OTHERS"]),
    name: z.string().min(3, "Name must be at least 3 characters long"),
    confirmPassword: z.string()
}).refine(data=>data.confirmPassword===data.password,
    {message:"Password and confirm password do not match",path:['confirmPassword']})
export function SignupForm({ ...props }: React.ComponentProps<typeof Card>) {
    type SignUpSchemaType=z.infer<typeof signUpSchema>;
    const {handleSubmit,register,formState:{errors}}=useForm<SignUpSchemaType>({
        resolver: zodResolver(signUpSchema),
        defaultValues:{name:"",password:"",email:'',gender:'MALE',confirmPassword:''}
    })
    const {mutate:signUp,isPending}=useSignup();
    const onSubmit=(data:SignUpSchemaType)=>{
        const registrationParams: RegisterRequest={
            name: data.name,
            gender: data.gender,
            password: data.password,
            image: null,
            email: data.email
        }
        signUp(registrationParams,{
            onError:(error:any)=>{
                console.log(error)
                const errorData=error.response?.data as ApiError;
                toast.error("Registration Failed",{
                    description: errorData.message
                })
            }
        })
    }
    return (
        <Card {...props}>
            <CardHeader>
                <CardTitle>Create an account</CardTitle>
                <CardDescription>
                    Enter your information below to create your account
                </CardDescription>
            </CardHeader>
            <CardContent>
                <form onSubmit={handleSubmit(onSubmit)}>
                    <FieldGroup>
                        <Field>
                            <FieldLabel htmlFor="name">Full Name</FieldLabel>
                            <Input id="name" type="text" placeholder="John Doe" {...register('name')} required />
                            {errors.name && (
                                <p className="text-sm font-medium text-destructive">{errors.name.message}</p>
                            )}
                        </Field>
                        <Field>
                            <FieldLabel htmlFor="email">Email</FieldLabel>
                            <Input
                                id="email"
                                type="email"
                                placeholder="m@example.com"
                                required
                                {...register('email')}
                            />
                            <FieldDescription>
                                We&apos;ll use this to contact you. We will not share your email
                                with anyone else.
                            </FieldDescription>
                            {errors.email && (
                                <p className="text-sm font-medium text-destructive">{errors.email.message}</p>
                            )}
                        </Field>
                        <Field>
                            <FieldLabel  htmlFor="gender">Gender</FieldLabel>
                            <Select {...register('gender')}>
                                <SelectTrigger className="w-full">
                                    <SelectValue placeholder="Select Gender" />
                                </SelectTrigger>
                                <SelectContent className={'dark'}>
                                    <SelectItem value="MALE">Male</SelectItem>
                                    <SelectItem value="FEMALE">Female</SelectItem>
                                    <SelectItem value="OTHERS">Others</SelectItem>
                                </SelectContent>
                            </Select>
                            {errors.gender && (
                                <p className="text-sm font-medium text-destructive">{errors.gender.message}</p>
                            )}
                        </Field>
                        <Field>
                            <FieldLabel htmlFor="password">Password</FieldLabel>
                            <Input id="password" type="password" {...register('password')} required />
                            <FieldDescription>
                                Must be between 5 and 15 characters long.
                            </FieldDescription>
                            {errors.password && (
                                <p className="text-sm font-medium text-destructive">{errors.password.message}</p>
                            )}
                        </Field>
                        <Field>
                            <FieldLabel htmlFor="confirm-password">
                                Confirm Password
                            </FieldLabel>
                            <Input id="confirm-password" type="password" {...register('confirmPassword')} required />
                            <FieldDescription>Please confirm your password.</FieldDescription>
                            {errors.confirmPassword && (
                                <p className="text-sm font-medium text-destructive">{errors.confirmPassword.message}</p>
                            )}
                        </Field>
                        <FieldGroup>
                            <Field>
                                <Button disabled={isPending}  type="submit">
                                    {isPending?<Spinner/>:"Create Account"}
                                </Button>
                                <span className="my-0.5 mx-auto block h-px w-full bg-border opacity-50" />
                                <Button variant="outline" onClick={()=>
                                    window.location.href="http://localhost:8082/api/oauth2/authorization/google"}
                                        type="button">
                                    Login with Google
                                </Button>
                                <Button variant="outline" onClick={()=>window.location
                                    .href="http://localhost:8082/api/oauth2/authorization/github"} type="button">
                                    Login with Github
                                </Button>
                                <FieldDescription className="px-6 text-center">
                                    Already have an account?
                                    <NavLink className={`ml-2`} to={`/login`}>Login</NavLink>
                                </FieldDescription>
                            </Field>
                        </FieldGroup>
                    </FieldGroup>
                </form>
            </CardContent>
        </Card>
    )
}
