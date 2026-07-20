import { createFileRoute, Link, useNavigate } from "@tanstack/react-router";
import { useState } from "react";
import { useMutation, useQuery } from "@tanstack/react-query";
import { ArrowRight, Building2, Loader2, Lock, Mail, ShieldCheck, Sparkles, User } from "lucide-react";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { api } from "@/lib/api";
import { normalizeSession, setSession } from "@/lib/auth";

export const Route = createFileRoute("/register")({
  head: () => ({
    meta: [
      { title: "Create account — Resolvr" },
      {
        name: "description",
        content: "Register as a student and start raising campus complaints on Resolvr.",
      },
    ],
  }),
  component: RegisterPage,
});

function RegisterPage() {
  const navigate = useNavigate();
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [deptId, setDeptId] = useState("");

  const departments = useQuery({
    queryKey: ["academic-departments"],
    queryFn: api.getAcademicDepartments,
  });

  const register = useMutation({
    mutationFn: () =>
      api.register({
        userName: name,
        userEmail: email,
        userPassword: password,
        academicDepartmentId: Number(deptId),
      }),
    onSuccess: async (user) => {
      const session = normalizeSession(user);
      const resolvedSession = {
        ...session,
        userName: session.userName || name.trim() || "User",
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
          toast.success("Account created", {
            description: "You are signed in and ready to raise complaints.",
          });
          navigate({ to: "/student" });
          return;
        }
      }

      if (!resolvedSession.userId) {
        toast.error("Signup succeeded, but the backend did not return a user id.");
        return;
      }

      setSession(resolvedSession);
      toast.success("Account created", {
        description: "You are signed in and ready to raise complaints.",
      });
      navigate({ to: "/student" });
    },
    onError: (error: Error) => toast.error(error.message),
  });

  return (
    <div className="min-h-screen bg-[radial-gradient(circle_at_15%_12%,rgba(34,67,148,0.08)_0%,transparent_30%),radial-gradient(circle_at_88%_18%,rgba(245,178,60,0.14)_0%,transparent_24%),linear-gradient(180deg,#f6f8fc_0%,#f4f7fc_100%)] px-6 py-8 sm:px-8 lg:px-10">
      <div className="mx-auto grid min-h-[calc(100vh-4rem)] w-full max-w-7xl items-center gap-6 lg:grid-cols-[0.96fr_1.04fr]">
        <section className="order-2 lg:order-1">
          <Card className="overflow-hidden border-border/70 bg-white/90 shadow-[0_20px_70px_rgba(15,23,42,0.12)] backdrop-blur-sm">
            <CardContent className="p-0">
              <div className="grid min-h-[680px] lg:grid-cols-[1.05fr_0.95fr]">
                <div className="relative min-h-[260px] overflow-hidden bg-[linear-gradient(180deg,#18316d_0%,#10224f_100%)] p-6 text-white lg:min-h-full">
                  <div className="absolute inset-0 bg-[radial-gradient(circle_at_top_left,rgba(255,255,255,0.22),transparent_32%),radial-gradient(circle_at_bottom_right,rgba(245,178,60,0.26),transparent_28%)]" />
                  <div className="relative z-10 flex h-full flex-col justify-between">
                    <div className="flex items-center gap-3">
                      <div className="grid h-12 w-12 place-items-center rounded-2xl bg-white/12 backdrop-blur-sm">
                        <ShieldCheck className="h-6 w-6 text-white" />
                      </div>
                      <div>
                        <div className="font-display text-xl font-semibold tracking-tight">Resolvr</div>
                        <div className="text-[10px] uppercase tracking-[0.32em] text-white/60">
                          Complaint portal
                        </div>
                      </div>
                    </div>

                    <div className="space-y-5">
                      <div className="inline-flex items-center gap-2 rounded-full border border-white/15 bg-white/10 px-3 py-1 text-xs font-medium text-white/90 backdrop-blur-sm">
                        <Sparkles className="h-3.5 w-3.5 text-accent" />
                        Student registration
                      </div>
                      <div>
                        <h2 className="max-w-xs text-3xl font-bold leading-tight tracking-tight md:text-4xl">
                          Join the complaint portal in minutes.
                        </h2>
                        <p className="mt-3 max-w-sm text-sm leading-7 text-white/72">
                          Create your account, choose your academic department, and start raising
                          issues with a workflow designed for real use.
                        </p>
                      </div>
                    </div>

                    <div className="grid gap-3 pt-8 text-sm text-white/88">
                      {[
                        "Track complaint history from your dashboard",
                        "Submit feedback after a case is resolved",
                        "Work with a clean student-focused interface",
                      ].map((item) => (
                        <div key={item} className="flex items-center gap-2">
                          <div className="h-2 w-2 rounded-full bg-accent" />
                          <span>{item}</span>
                        </div>
                      ))}
                    </div>
                  </div>
                </div>

                <div className="flex items-center justify-center px-5 py-7 sm:px-8">
                  <div className="w-full max-w-md">
                    <div className="mb-8 text-center">
                      <div className="mx-auto mb-4 grid h-14 w-14 place-items-center rounded-2xl bg-primary/10 text-primary">
                        <User className="h-6 w-6" />
                      </div>
                      <h3 className="text-3xl font-bold tracking-tight text-foreground">Create account</h3>
                      <p className="mt-2 text-sm leading-6 text-muted-foreground">
                        Students only. Register with your campus email.
                      </p>
                    </div>

                    <form
                      className="space-y-4"
                      onSubmit={(event) => {
                        event.preventDefault();
                        if (!deptId) {
                          toast.error("Please select your academic department.");
                          return;
                        }
                        register.mutate();
                      }}
                    >
                      <div className="space-y-2">
                        <Label htmlFor="name">Full name</Label>
                        <div className="relative">
                          <User className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                          <Input
                            id="name"
                            required
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            placeholder="Alex Johnson"
                            className="h-11 rounded-2xl pl-9"
                          />
                        </div>
                      </div>

                      <div className="space-y-2">
                        <Label htmlFor="email">Email</Label>
                        <div className="relative">
                          <Mail className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                          <Input
                            id="email"
                            type="email"
                            required
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            placeholder="you@campus.edu"
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
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="At least 8 characters"
                            className="h-11 rounded-2xl pl-9"
                          />
                        </div>
                      </div>

                      <div className="space-y-2">
                        <Label>Academic department</Label>
                        <div className="relative">
                          <Building2 className="pointer-events-none absolute left-3 top-1/2 z-10 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                          <Select value={deptId} onValueChange={setDeptId}>
                            <SelectTrigger className="h-11 rounded-2xl pl-9">
                              <SelectValue
                                placeholder={
                                  departments.isLoading ? "Loading departments..." : "Select your department"
                                }
                              />
                            </SelectTrigger>
                            <SelectContent>
                              {departments.data?.map((department) => (
                                <SelectItem
                                  key={department.academicDepartmentId}
                                  value={String(department.academicDepartmentId)}
                                >
                                  {department.academicDepartmentName}
                                </SelectItem>
                              ))}
                            </SelectContent>
                          </Select>
                        </div>
                        {departments.isError ? (
                          <p className="text-xs text-destructive">
                            Couldn't load departments. Please check the API server.
                          </p>
                        ) : null}
                      </div>

                      <Button
                        type="submit"
                        disabled={register.isPending}
                        className="group mt-2 h-11 w-full rounded-2xl bg-[image:var(--gradient-hero)] text-white hover:opacity-95 hover:shadow-[var(--shadow-elegant)]"
                      >
                        {register.isPending ? (
                          <Loader2 className="h-4 w-4 animate-spin" />
                        ) : (
                          <>
                            Create account
                            <ArrowRight className="ml-1 h-4 w-4 transition-transform group-hover:translate-x-0.5" />
                          </>
                        )}
                      </Button>
                    </form>

                    <div className="mt-8 rounded-2xl border border-border/70 bg-muted/30 p-4 text-center text-sm text-muted-foreground">
                      Already registered?{" "}
                      <Link to="/" className="font-medium text-primary hover:underline">
                        Sign in
                      </Link>
                    </div>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </section>
      </div>
    </div>
  );
}
