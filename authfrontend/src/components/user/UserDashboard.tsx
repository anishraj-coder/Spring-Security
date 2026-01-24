import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { useUserProfile } from "@/hooks/useUser";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger, DialogFooter } from "@/components/ui/dialog";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Spinner } from "@/components/ui/spinner";
import { Controller } from "react-hook-form";

const profileSchema = z.object({
    name: z.string().min(2, "Name is too short"),
    gender: z.enum(["MALE", "FEMALE", "OTHER"]),
    email: z.email("Email is not valid"),
});

type ProfileFormValues = z.infer<typeof profileSchema>;

const UserDashboard = () => {
    const { profile, updateProfile } = useUserProfile();
    const [open, setOpen] = useState(false);

    const form = useForm<ProfileFormValues>({
        resolver: zodResolver(profileSchema),
        values: {
            name: profile?.data?.name || "",
            gender: profile?.data?.gender as 'MALE'|'FEMALE'|'OTHER' || "OTHER",
            email: profile?.data?.email||""
        },
    });

    if (profile.isLoading) return <div className="flex justify-center p-20"><Spinner /></div>;

    const onSubmit = (data: ProfileFormValues) => {
        updateProfile.mutate(data, {
            onSuccess: () => setOpen(false),
        });
    };

    return (
        <div className="max-w-2xl mx-auto py-10">
            <Card>
                <CardHeader className="flex flex-row items-center gap-4">
                    <Avatar className="h-16 w-16">
                        <AvatarImage src={profile.data?.image || ""} />
                        <AvatarFallback>{profile.data?.name?.[0]}</AvatarFallback>
                    </Avatar>
                    <div>
                        <CardTitle className="text-2xl">{profile.data?.name}</CardTitle>
                        <CardDescription>{profile.data?.email}</CardDescription>
                    </div>
                </CardHeader>
                <CardContent className="space-y-4">
                    <div className="grid grid-cols-2 gap-4 text-sm">
                        <div>
                            <p className="text-muted-foreground">Gender</p>
                            <p className="font-medium capitalize">{profile.data?.gender?.toLowerCase() || "Not specified"}</p>
                        </div>
                        <div>
                            <p className="text-muted-foreground">Account Status</p>
                            <p className="text-green-500 font-medium">Active</p>
                        </div>
                    </div>

                    <Dialog open={open} onOpenChange={setOpen}>
                        <DialogTrigger asChild>
                            <Button className="w-full">Edit Profile</Button>
                        </DialogTrigger>
                        <DialogContent>
                            <DialogHeader>
                                <DialogTitle>Edit Profile</DialogTitle>
                            </DialogHeader>
                            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4 py-4">
                                <div className="space-y-2">
                                    <Label htmlFor="name">Full Name</Label>
                                    <Input id="name" {...form.register("name")} />
                                    {form.formState.errors.name && <p className="text-xs text-destructive">{form.formState.errors.name.message}</p>}
                                </div>
                                <div className="space-y-2">
                                    <Label htmlFor="name">Full Name</Label>
                                    <Input id="name" {...form.register("email")} />
                                    {form.formState.errors.email && <p className="text-xs text-destructive">{form.formState.errors.email.message}</p>}
                                </div>

                                <div className="space-y-2">
                                    <Label>Gender</Label>
                                    <Controller
                                        control={form.control}
                                        name="gender"
                                        render={({ field }) => (
                                            <Select onValueChange={field.onChange} defaultValue={field.value}>
                                                <SelectTrigger>
                                                    <SelectValue placeholder="Select gender" />
                                                </SelectTrigger>
                                                <SelectContent>
                                                    <SelectItem value="MALE">Male</SelectItem>
                                                    <SelectItem value="FEMALE">Female</SelectItem>
                                                    <SelectItem value="OTHER">Other</SelectItem>
                                                </SelectContent>
                                            </Select>
                                        )}
                                    />
                                </div>
                                <DialogFooter>
                                    <Button type="submit" disabled={updateProfile.isPending}>
                                        {updateProfile.isPending ? "Saving..." : "Save Changes"}
                                    </Button>
                                </DialogFooter>
                            </form>
                        </DialogContent>
                    </Dialog>
                </CardContent>
            </Card>
        </div>
    );
};

export default UserDashboard;