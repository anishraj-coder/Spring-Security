// src/components/error/ErrorFallback.tsx
import { Button } from "@/components/ui/button";
import { AlertCircle, RefreshCw, Home, ChevronRight } from "lucide-react";
import type { FallbackProps } from "react-error-boundary"; // Import the correct type

export const ErrorFallback = ({ error, resetErrorBoundary }: FallbackProps) => {
    return (
        <div className="relative flex h-screen w-full flex-col items-center justify-center bg-background p-6 text-center text-foreground dark overflow-hidden">
            {/* Background Decorative Element */}
            <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[500px] h-[500px] bg-primary/5 rounded-full blur-[120px] pointer-events-none" />

            <div className="relative z-10 max-w-lg w-full space-y-8 animate-in fade-in zoom-in duration-500">
                {/* Icon & Heading */}
                <div className="flex flex-col items-center space-y-4">
                    <div className="rounded-full bg-destructive/10 p-4 ring-1 ring-destructive/20 shadow-[0_0_20px_rgba(239,68,68,0.1)]">
                        <AlertCircle className="h-10 w-10 text-destructive" />
                    </div>
                    <div className="space-y-2">
                        <h1 className="text-4xl font-extrabold tracking-tight sm:text-5xl">
                            Oops! Something went wrong
                        </h1>
                        <p className="text-muted-foreground text-lg">
                            The application encountered an unexpected error.
                            Don't worry, your data is safe.
                        </p>
                    </div>
                </div>

                {/* Technical Details Box */}
                <div className="group relative overflow-hidden rounded-xl border border-border bg-card/50 backdrop-blur-sm transition-all hover:border-border/80">
                    <div className="flex items-center justify-between border-b border-border bg-muted/30 px-4 py-2">
                        <div className="flex items-center gap-2">
                            <div className="h-2 w-2 rounded-full bg-destructive/50" />
                            <span className="text-[10px] font-bold uppercase tracking-wider text-muted-foreground">Error Log</span>
                        </div>
                        <span className="text-[10px] font-mono text-muted-foreground/50">v1.0.4</span>
                    </div>
                    <div className="p-4 text-left">
                        <code className="block font-mono text-sm text-destructive/90 break-all leading-relaxed">
                            <span className="text-muted-foreground mr-2 select-none">$</span>
                            {/* We cast to string here safely */}
                            {String( error || "Unknown runtime error occurred.")}
                        </code>
                    </div>
                </div>

                {/* Actions */}
                <div className="flex flex-col sm:flex-row items-center justify-center gap-4 pt-4">
                    <Button
                        size="lg"
                        onClick={resetErrorBoundary}
                        className="w-full sm:w-auto min-w-[140px] shadow-lg shadow-primary/20 transition-all hover:scale-[1.02] active:scale-[0.98]"
                    >
                        <RefreshCw className="mr-2 h-4 w-4" />
                        Try Again
                    </Button>
                    <Button
                        variant="outline"
                        size="lg"
                        onClick={() => (window.location.href = "/")}
                        className="w-full sm:w-auto min-w-[140px] group transition-all hover:bg-muted"
                    >
                        <Home className="mr-2 h-4 w-4 transition-transform group-hover:-translate-y-0.5" />
                        Go Home
                        <ChevronRight className="ml-1 h-3 w-3 opacity-0 -translate-x-2 transition-all group-hover:opacity-100 group-hover:translate-x-0" />
                    </Button>
                </div>

                {/* Footer help text */}
                <p className="text-xs text-muted-foreground pt-4">
                    If this issue persists, please contact our support team with the error log above.
                </p>
            </div>
        </div>
    );
};