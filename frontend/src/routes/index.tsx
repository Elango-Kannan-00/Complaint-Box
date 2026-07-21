import { createFileRoute, Link, useNavigate } from "@tanstack/react-router";
import { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { ArrowRight, Loader2, Lock, Mail, ShieldCheck } from "lucide-react";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { api } from "@/lib/api";
import { normalizeSession, setSession } from "@/lib/auth";

export const Route = createFileRoute("/")({
  head: () => ({
    meta: [
      { title: "Resolvr — Sign in" },
      {
        name: "description",
        content: "Sign in to ResolveR, the smart complaint management platform for campuses.",
      },
    ],
  }),
  component: LandingPage,
});

function LandingPage() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const login = useMutation({
    mutationFn: () => api.login({ userEmail: email, userPassword: password }),
    onSuccess: async (user) => {
      const session = normalizeSession(user);
      const resolvedSession = {
        ...session,
        userName: session.userName || "User",
        userEmail: session.userEmail || email.trim(),
      };

      if (!resolvedSession.userId && resolvedSession.userEmail) {
        const profileSession = normalizeSession(await api.getUserProfileByEmail(resolvedSession.userEmail));
        if (profileSession.userId) {
          setSession({
            ...profileSession,
            userName: profileSession.userName || resolvedSession.userName,
            userEmail: profileSession.userEmail || resolvedSession.userEmail,
          });
          toast.success("Welcome back", {
            description: `Signed in as ${formatRoleLabel(profileSession.userRole)}`,
          });
          navigate({ to: routeForRole(profileSession.userRole) });
          return;
        }
      }

      if (!resolvedSession.userId) {
        toast.error("Login succeeded, but the backend did not return a user id.");
        return;
      }

      setSession(resolvedSession);
      toast.success("Welcome back", {
        description: `Signed in as ${formatRoleLabel(resolvedSession.userRole)}`,
      });
      navigate({ to: routeForRole(resolvedSession.userRole) });
    },
    onError: (error: Error) => toast.error(error.message),
  });

  return (
    <div className="min-h-screen bg-[radial-gradient(circle_at_15%_12%,rgba(34,67,148,0.08)_0%,transparent_30%),radial-gradient(circle_at_88%_18%,rgba(245,178,60,0.14)_0%,transparent_24%),linear-gradient(180deg,#f6f8fc_0%,#f4f7fc_100%)] px-6 py-8 sm:px-8 lg:px-10">
      <div className="mx-auto flex min-h-[calc(100vh-4rem)] max-w-md items-center">
        <Card className="w-full overflow-hidden border-border/70 bg-white/92 shadow-[0_20px_70px_rgba(15,23,42,0.12)] backdrop-blur-sm">
          <CardHeader className="space-y-3 text-center">
            <div className="mx-auto grid h-14 w-14 place-items-center rounded-2xl bg-[image:var(--gradient-hero)] text-white shadow-[var(--shadow-elegant)]">
              <ShieldCheck className="h-6 w-6" />
            </div>
            <CardTitle className="font-display text-3xl">Sign in</CardTitle>
            <CardDescription>
              Use your campus email and password to access ResolveR.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <form
              className="space-y-4"
              onSubmit={(event) => {
                event.preventDefault();
                login.mutate();
              }}
            >
              <div className="space-y-2">
                <Label htmlFor="email">Email</Label>
                <div className="relative">
                  <Mail className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                  <Input
                    id="email"
                    type="email"
                    required
                    autoComplete="email"
                    placeholder="you@campus.edu"
                    value={email}
                    onChange={(event) => setEmail(event.target.value)}
                    className="h-11 rounded-2xl pl-9"
                  />
                </div>
              </div>

              <div className="space-y-2">
                <Label htmlFor="password">Password</Label>
                <div className="relative">
                  <Lock className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                  <Input
                    id="password"
                    type="password"
                    required
                    minLength={8}
                    autoComplete="current-password"
                    placeholder="At least 8 characters"
                    value={password}
                    onChange={(event) => setPassword(event.target.value)}
                    className="h-11 rounded-2xl pl-9"
                  />
                </div>
              </div>

              <Button
                type="submit"
                disabled={login.isPending}
                className="group mt-2 h-11 w-full rounded-2xl bg-[image:var(--gradient-hero)] text-white hover:opacity-95 hover:shadow-[var(--shadow-elegant)]"
              >
                {login.isPending ? (
                  <Loader2 className="h-4 w-4 animate-spin" />
                ) : (
                  <>
                    Sign in
                    <ArrowRight className="ml-1 h-4 w-4 transition-transform group-hover:translate-x-0.5" />
                  </>
                )}
              </Button>
            </form>

            <div className="mt-6 rounded-2xl border border-border/70 bg-muted/30 p-4 text-center text-sm text-muted-foreground">
              New here?{" "}
              <Link to="/register" className="font-medium text-primary hover:underline">
                Create an account
              </Link>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}

function formatRoleLabel(role: string) {
  return role
    .toLowerCase()
    .replaceAll("_", " ")
    .replace(/\b\w/g, (char) => char.toUpperCase());
}

function routeForRole(role: string) {
  if (role === "STUDENT") return "/student";
  if (role === "HOD") return "/hod";
  return "/student";
}
