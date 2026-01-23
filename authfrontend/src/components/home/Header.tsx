import {
    NavigationMenu,
    NavigationMenuItem,
    NavigationMenuList,
} from "@/components/ui/navigation-menu"
import {buttonVariants} from "@/components/ui/button"
import {NavLink} from "react-router"
import {cn} from "@/lib/utils.ts";
import {Activity} from "react";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import {useAuthStore} from "@/store/useAuthStore.ts";

const Header = () => {
    const user=useAuthStore(state=>state.user);
    const isLoggedIn=!!user;

    return (
        <header className="w-full h-16 border-b sticky top-0 backdrop-blur-lg">
            <div className={`h-full flex justify-between px-10`}>
                <NavigationMenu>
                    <NavigationMenuList>
                        <NavigationMenuItem>
                            <h1 className={`text-xl font-bold`}>Text</h1>
                        </NavigationMenuItem>
                    </NavigationMenuList>
                </NavigationMenu>
                <NavigationMenu>
                    <NavigationMenuList className={`gap-10`}>
                        <Activity mode={!isLoggedIn?'visible':'hidden'}>
                            <NavigationMenuItem>
                                <NavLink to={`/login`}
                                         className={({isActive})=>
                                             cn(buttonVariants({variant: isActive?'outline':'ghost'}))}>
                                    Login
                                </NavLink>
                            </NavigationMenuItem>
                            <NavigationMenuItem>
                                <NavLink to={`/signup`} className={({isActive})=>
                                    cn(buttonVariants({variant: isActive?"secondary":"outline"}))}>
                                    Signup
                                </NavLink>
                            </NavigationMenuItem>
                        </Activity>
                        <Activity mode={isLoggedIn?'visible':'hidden'}>
                            <Avatar >
                                <AvatarImage src={user?.image} />
                                <AvatarFallback>{user?.name[0]}</AvatarFallback>
                            </Avatar>
                        </Activity>
                    </NavigationMenuList>
                </NavigationMenu>
            </div>
        </header>
    )
}

export default Header
