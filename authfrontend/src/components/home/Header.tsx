import {
    NavigationMenu,
    NavigationMenuItem,
    NavigationMenuList,
} from "@/components/ui/navigation-menu"
import {buttonVariants} from "@/components/ui/button"
import {NavLink} from "react-router"
import {cn} from "@/lib/utils.ts";

const Header = () => {


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
                    </NavigationMenuList>
                </NavigationMenu>
            </div>
        </header>
    )
}

export default Header
