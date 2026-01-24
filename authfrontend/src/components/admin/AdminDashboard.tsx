import {useState} from "react";
import {useGetAllUsers, useAdminActions} from "@/hooks/useAdminActions";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogFooter,
} from "@/components/ui/dialog";
import {Button} from "@/components/ui/button";
import {Spinner} from "@/components/ui/spinner";
import {Badge} from "@/components/ui/badge";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import type {User} from "@/utils/constants.ts";

const AdminDashboard = () => {
    const [page, setPage] = useState(0);
    const [selectedUser, setSelectedUser] = useState<User | null>(null);
    const {data, isLoading} = useGetAllUsers(page);
    const {verifyUser, deleteUser} = useAdminActions();

    if (isLoading) return <div className="flex justify-center p-10"><Spinner/></div>;

    const handleVerify = (user: User) => {
        user.enabled=true
        verifyUser.mutate(user.id, {
            onSuccess: () => setSelectedUser(null) // Close dialog on success
        });
    };

    const handleDelete = (userId: string) => {
        deleteUser.mutate(userId, {
            onSuccess: () => setSelectedUser(null)
        });
    };

    return (
        <div className="space-y-4 dark">
            <h1 className="text-4xl border-b border-border w-4/5 mx-auto font-bold text-center py-5">
                User Management
            </h1>

            <div className="rounded-md border mx-auto w-4/5">
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead>User</TableHead>
                            <TableHead>Email</TableHead>
                            <TableHead>Status</TableHead>
                            <TableHead className="text-right">Action</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {data?.content.map((user) => (
                            <TableRow
                                key={user.email}
                                className="cursor-pointer hover:bg-muted/50"
                                onClick={() => setSelectedUser(user)}
                            >
                                <TableCell className="flex items-center gap-3">
                                    <Avatar className="h-8 w-8">
                                        <AvatarImage src={user.image ?? ""}/>
                                        <AvatarFallback>{user.name[0]}</AvatarFallback>
                                    </Avatar>
                                    <span className="font-medium">{user.name}</span>
                                </TableCell>
                                <TableCell>{user.email}</TableCell>
                                <TableCell>
                                    <Badge variant={user.enabled ? "default" : "destructive"}>
                                        {user.enabled ? "Verified" : "Pending"}
                                    </Badge>
                                </TableCell>
                                <TableCell className="text-right">
                                    <Button variant="ghost" size="sm">View Details</Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </div>

            {/* Pagination Controls... */}
            <div className="flex items-center justify-center mx-auto w-4/5 border-border border-y space-x-2 py-4">
                <Button
                    variant="outline"
                    size="sm"
                    onClick={() => setPage(p => p - 1)}
                    disabled={page === 0}
                >
                    Previous
                </Button>
                <span className="text-sm">Page {page + 1} of {data?.totalPages}</span>
                <Button
                    variant="outline"
                    size="sm"
                    onClick={() => setPage(p => p + 1)}
                    disabled={data?.last}
                >
                    Next
                </Button>
            </div>
            {/* --- USER DETAIL DIALOG --- */}
            <Dialog open={!!selectedUser} onOpenChange={() => setSelectedUser(null)}>
                <DialogContent className="sm:max-w-106.25 bg-gray-900 text-gray-100 border border-gray-700">
                    <DialogHeader>
                        <DialogTitle className="text-gray-100">User Profile</DialogTitle>
                        <DialogDescription className="text-gray-300">
                            Full account details for {selectedUser?.name}.
                        </DialogDescription>
                    </DialogHeader>

                    {selectedUser && (
                        <div className="grid gap-4 py-4">
                            <div className="flex items-center justify-center pb-4 dark">
                                <Avatar className="h-20 w-20 border-2 border-gray-600 dark">
                                    <AvatarImage src={selectedUser.image ?? ""}/>
                                    <AvatarFallback className="text-2xl text-gray-100">
                                        {selectedUser.name[0]}
                                    </AvatarFallback>
                                </Avatar>
                            </div>

                            <div className="grid grid-cols-4 gap-4 text-sm text-gray-100">
                                <span className="font-bold">ID:</span>
                                <span className="col-span-3 font-mono text-xs text-gray-300">
                                    {selectedUser.id}
                                </span>
                            </div>

                            <div className="grid grid-cols-4 gap-4 text-sm text-gray-100">
                                <span className="font-bold">Email:</span>
                                <span className="col-span-3 text-gray-300">
                                    {selectedUser.email}
                                </span>
                            </div>

                            <div className="grid grid-cols-4 gap-4 text-sm text-gray-100">
                                <span className="font-bold">Gender:</span>
                                <span className="col-span-3 capitalize text-gray-300">
                                    {selectedUser?.gender?.toLowerCase() ?? "not specified"}
                                </span>
                            </div>

                            <div className="grid grid-cols-4 gap-4 text-sm text-gray-100">
                                <span className="font-bold">Roles:</span>
                                <div className="col-span-3 flex gap-1">
                                    {selectedUser.roles.map(role => (
                                        <Badge key={role} variant="outline" className="text-gray-100 border-gray-500">
                                            {role}
                                        </Badge>
                                    ))}
                                </div>
                            </div>
                        </div>
                    )}

                    <DialogFooter className="flex flex-col sm:flex-row gap-2">
                        <Button
                            variant="destructive"
                            className="w-full sm:w-auto bg-red-600 hover:bg-red-700 text-white"
                            onClick={() => selectedUser && handleDelete(selectedUser.id)}
                            disabled={deleteUser.isPending}
                        >
                            {deleteUser.isPending ? "Deleting..." : "Delete User"}
                        </Button>

                        {!selectedUser?.enabled && (
                            <Button
                                className="w-full sm:w-auto bg-indigo-600 hover:bg-indigo-700 text-white"
                                onClick={() => selectedUser && handleVerify(selectedUser)}
                                disabled={verifyUser.isPending}
                            >
                                {verifyUser.isPending ? "Verifying..." : "Verify User"}
                            </Button>
                        )}
                    </DialogFooter>
                </DialogContent>
            </Dialog>

        </div>
    );
};

export default AdminDashboard;