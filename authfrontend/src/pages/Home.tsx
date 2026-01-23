import { Button } from "@/components/ui/button";
import {
    Card,
    CardContent,
    CardHeader,
    CardTitle,
    CardDescription,
} from "@/components/ui/card";
import { Shield, Globe, Zap, Lock, Fingerprint, Key } from "lucide-react";
import { useNavigate } from "react-router";

const Home = () => {
    const navigate = useNavigate();

    return (
        <div className="flex flex-col items-center w-full">

            {/* --- HERO SECTION --- */}
            <section className="flex flex-col items-center text-center py-20 px-4 md:py-32 gap-6 max-w-4xl">
                {/* The "Pill" Badge */}
                <div className="inline-flex items-center rounded-full border px-2.5 py-0.5 text-xs font-semibold transition-colors focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 border-transparent bg-secondary text-secondary-foreground hover:bg-secondary/80">
                    ✨ Secure auth made simple
                </div>

                {/* Main Headline - Tracking tight makes big text look premium */}
                <h1 className="text-4xl md:text-6xl font-bold tracking-tight">
                    Classic authentication for modern apps
                </h1>

                {/* Subheadline - Muted foreground reduces visual noise */}
                <p className="text-lg text-muted-foreground max-w-2xl">
                    Password, OTP, and social sign-in with token refresh baked in. Drop-in
                    UI, clean APIs, and production-grade security.
                </p>

                {/* CTA Buttons */}
                <div className="flex gap-4 mt-4">
                    <Button size="lg" onClick={() => navigate("/signup")}>
                        Get started free &rarr;
                    </Button>
                    <Button size="lg" variant="outline" onClick={() => navigate("/login")}>
                        Login
                    </Button>
                </div>

                <p className="text-xs text-muted-foreground mt-2">
                    No credit card required · 14-day trial
                </p>
            </section>


            {/* --- FEATURE STRIP (The dark bar in the middle) --- */}
            <section className="w-full px-4 md:px-10">
                <div className="w-full border rounded-xl bg-card/50 p-8 flex flex-wrap justify-around gap-6 md:gap-0">
                    <FeatureItem icon={<Shield className="h-5 w-5" />} text="ISO-ready" />
                    <FeatureItem icon={<Globe className="h-5 w-5" />} text="OAuth & OIDC" />
                    <FeatureItem icon={<Zap className="h-5 w-5" />} text="99.99% Uptime" />
                    <FeatureItem icon={<Lock className="h-5 w-5" />} text="SSO & MFA" />
                </div>
            </section>


            {/* --- FEATURE CARDS GRID --- */}
            <section className="py-20 px-4 md:px-10 w-full max-w-7xl">
                <div className="text-center mb-12">
                    <h2 className="text-3xl font-bold tracking-tight mb-4">Why choose Auth App?</h2>
                    <p className="text-muted-foreground">Everything you need to plug authentication into your product.</p>
                </div>

                {/* Grid: 1 column on mobile, 3 columns on desktop */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                    <FeatureCard
                        icon={<Lock className="h-8 w-8 mb-4 text-primary" />}
                        title="Secure by default"
                        description="HttpOnly cookies, short-lived JWTs, and sane defaults to keep your data safe."
                    />
                    <FeatureCard
                        icon={<Fingerprint className="h-8 w-8 mb-4 text-primary" />}
                        title="MFA & OTP"
                        description="Email/SMS OTP, TOTP, and backup codes to keep accounts safe."
                    />
                    <FeatureCard
                        icon={<Key className="h-8 w-8 mb-4 text-primary" />}
                        title="Social sign-in"
                        description="Google, GitHub, Apple, and more with one configuration."
                    />
                </div>
            </section>

        </div>
    );
};

// --- Helper Components to keep the code clean ---

const FeatureItem = ({ icon, text }: { icon: React.ReactNode; text: string }) => (
    <div className="flex items-center gap-2 text-muted-foreground font-medium">
        {icon}
        <span>{text}</span>
    </div>
);

const FeatureCard = ({ icon, title, description }: { icon: React.ReactNode; title: string; description: string }) => (
    <Card className="bg-card/50 border-border/50">
        <CardHeader>
            <div className="mb-2">{icon}</div>
            <CardTitle>{title}</CardTitle>
        </CardHeader>
        <CardContent>
            <CardDescription className="text-base">
                {description}
            </CardDescription>
        </CardContent>
    </Card>
);

export default Home;