import {
    NavigationMenu,
    NavigationMenuItem,
    NavigationMenuList,
} from "@/components/ui/navigation-menu"
import {Button, buttonVariants} from "@/components/ui/button"
import {NavLink} from "react-router"
import {cn} from "@/lib/utils.ts";
import {Activity} from "react";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar"
import {useAuthStore} from "@/store/useAuthStore.ts";
import {useHover} from "@uidotdev/usehooks";
import {useLogout} from "@/hooks/useLogout.ts";
import {Spinner} from "@/components/ui/spinner.tsx";
import { useSidebar} from "@/components/ui/sidebar.tsx";
import {Menu, X} from "lucide-react";

const Header = () => {
    const user = useAuthStore(state => state.user);
    const isLoggedIn = !!user;
    const [ref, hovered] = useHover();
    const {mutate:logout,isPending}=useLogout();
    const isAdmin=user?.roles.some(role=>role.toUpperCase().includes('ADMIN'));
    const {isMobile,openMobile,toggleSidebar}=useSidebar();

    return (
        <header className="w-full h-16 border-b sticky top-0 backdrop-blur-lg">
            <div className={`h-full flex justify-between px-10`}>
                <NavigationMenu>
                    <NavigationMenuList className={`gap-5`}>
                        {isMobile && (
                            <NavigationMenuItem>
                                <Button
                                    variant="ghost"
                                    size="icon"
                                    onClick={toggleSidebar}
                                    className="hover:bg-accent transition-all duration-300"
                                >
                                    {openMobile ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
                                    <span className="sr-only">Toggle Menu</span>
                                </Button>
                            </NavigationMenuItem>
                        )}
                        <NavigationMenuItem>
                            <NavLink to={'/'}>
                                <div className={`flex items-center h-full gap-5 cursor-pointer select-none`}>
                                    <img src={`/icon.svg`} className={`object-contain object-center h-full`} alt="icon"/>
                                    <h1 className={`text-2xl font-bold`}>Auth App</h1>
                                </div>
                            </NavLink>
                        </NavigationMenuItem>
                        {isLoggedIn&&<NavigationMenuItem>
                            <NavLink className={({isActive})=>
                                cn(buttonVariants({variant: isActive?'secondary':'outline'}))}
                                     to={'/dashboard'}>
                                Dashboard
                            </NavLink>
                        </NavigationMenuItem>}
                        {isAdmin&&<NavigationMenuItem>
                            <NavLink className={cn(buttonVariants({variant: 'destructive'}))}
                                to={'/admin'}>
                                ADMIN PANEL
                            </NavLink>
                        </NavigationMenuItem>}
                    </NavigationMenuList>
                </NavigationMenu>
                <NavigationMenu>
                    <NavigationMenuList className={`gap-10`}>
                        <Activity mode={!isLoggedIn &&!isMobile ? 'visible' : 'hidden'}>
                            <NavigationMenuItem>
                                <NavLink to={`/login`}
                                         className={({isActive}) =>
                                             cn(buttonVariants({variant: isActive ? 'outline' : 'ghost'}))}>
                                    Login
                                </NavLink>
                            </NavigationMenuItem>
                            <NavigationMenuItem>
                                <NavLink to={`/signup`} className={({isActive}) =>
                                    cn(buttonVariants({variant: isActive ? "secondary" : "outline"}))}>
                                    Signup
                                </NavLink>
                            </NavigationMenuItem>
                        </Activity>
                        {isLoggedIn &&!isMobile &&
                            <div ref={ref} className={`relative  `}>
                                <NavLink className={'cursor-pointer select-none'} to={'/dashboard'}>
                                    <Avatar>
                                        <AvatarImage src={user?.image}/>
                                        <AvatarFallback>{user?.name[0]}</AvatarFallback>
                                    </Avatar>
                                </NavLink>


                                {hovered && <div className={`absolute min-w-32 right-0
                                rounded-lg flex flex-col gap-1 -bottom-1 translate-y-full
                                 bg-background p-2 ring-1 ring-border`}>
                                    <h1 className={`${cn(buttonVariants({variant: 'ghost'}))} `}>
                                        {user?.name}
                                    </h1>
                                    <h1 className={cn(buttonVariants({variant: 'ghost'}))}>
                                        {user?.email}
                                    </h1>
                                    <Button disabled={isPending} onClick={()=>logout()}
                                            variant={'destructive'}>
                                        {isPending?<Spinner/>:"Log Out"}
                                    </Button>
                                </div>}

                            </div>}
                    </NavigationMenuList>
                </NavigationMenu>
            </div>
        </header>
    )
}

export default Header
