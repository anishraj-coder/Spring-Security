import {
    Sidebar,
    SidebarContent,
    SidebarFooter,
    SidebarHeader,
    SidebarMenu,
    SidebarMenuButton,
    SidebarMenuItem,
    SidebarGroup,
    SidebarGroupLabel, useSidebar,
} from "@/components/ui/sidebar"
import { useAuthStore } from "@/store/useAuthStore"
import { NavLink } from "react-router"
import {LogIn, UserPlus, LayoutDashboard, LogOut, User, X} from "lucide-react"
import {useShallow} from "zustand/react/shallow";

const AppSidebar=()=> {
    const { user, logout } = useAuthStore(useShallow(state => ({user:state.user,logout:state.logout})));
    const isLoggedIn = !!user
    const {toggleSidebar}=useSidebar();
    return (

        <Sidebar collapsible="offcanvas" >
            <SidebarHeader className="p-4 w-full ">
                <div className={`w-full flex justify-between`}>
                    <h2 className="text-xl font-bold tracking-tight">AuthApp</h2>
                    <button onClick={toggleSidebar}><X/></button>
                </div>

            </SidebarHeader>

            <SidebarContent >
                <SidebarGroup>
                    <SidebarGroupLabel>Navigation</SidebarGroupLabel>
                    <SidebarMenu>
                        {isLoggedIn ? (
                            <SidebarMenuItem>
                                <SidebarMenuButton asChild>
                                    <NavLink to="/dashboard">
                                        <LayoutDashboard />
                                        <span>Dashboard</span>
                                    </NavLink>
                                </SidebarMenuButton>
                            </SidebarMenuItem>
                        ) : (
                            <>
                                <SidebarMenuItem>
                                    <SidebarMenuButton asChild>
                                        <NavLink onClick={toggleSidebar} to="/login">
                                            <LogIn />
                                            <span>Login</span>
                                        </NavLink>
                                    </SidebarMenuButton>
                                </SidebarMenuItem>
                                <SidebarMenuItem>
                                    <SidebarMenuButton  asChild>
                                        <NavLink onClick={toggleSidebar}  to="/signup">
                                            <UserPlus />
                                            <span>Sign Up</span>
                                        </NavLink>
                                    </SidebarMenuButton>
                                </SidebarMenuItem>
                            </>
                        )}
                    </SidebarMenu>
                </SidebarGroup>
            </SidebarContent>

            <SidebarFooter className="p-4 border-t border-sidebar-border">
                {isLoggedIn ? (
                    <SidebarMenu>
                        <SidebarMenuItem>
                            <div className="flex items-center gap-3 px-2 py-2">
                                <div className="h-8 w-8 rounded-full bg-primary/10 flex items-center justify-center">
                                    <User className="h-4 w-4 text-primary" />
                                </div>
                                <div className="flex flex-col">
                                    <span className="text-sm font-medium">{user.name}</span>
                                    <span className="text-xs text-muted-foreground truncate w-32">{user.email}</span>
                                </div>
                            </div>
                            <SidebarMenuButton onClick={logout} className="text-destructive">
                                <LogOut />
                                <span>Logout</span>
                            </SidebarMenuButton>
                        </SidebarMenuItem>
                    </SidebarMenu>
                ) : (
                    <p className="text-xs text-center text-muted-foreground italic">
                        Secure Authentication v1.0
                    </p>
                )}
            </SidebarFooter>
        </Sidebar>
    )
}
export default  AppSidebar;