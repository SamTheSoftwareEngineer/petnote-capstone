import { createBrowserRouter, RouterProvider, Navigate } from "react-router-dom"
import { useState } from "react"
import Landing from "./Landing"
import SignUpForm from "./SignUpForm"
import LoginForm from "./LoginForm"
import VerifyPage from "./VerifyPage"
import Layout from "./Layout"
import Dashboard from "./Dashboard"
import Logout from "./Logout"
import NotFound from "./NotFound"
import PetList from "./PetList"
import AddPetForm from "./AddPetForm"
import EditPetForm from "./EditPetForm"
import AddActivityForm from "./AddActivityForm"
import EditActivityForm from "./EditActivityForm"
import Stats from "./Stats"



const AppRouter = () => {
    const [user, setUser] = useState(JSON.parse(localStorage.getItem("user")));
    
    const routes = [

        // Can access when logged out
        {
            path: "/",
            element: user ? <Navigate to="/dashboard" /> : <Landing />,
        },
        {
            path: "/login",
            element: user ? <Navigate to="/dashboard" /> : <LoginForm setUser={setUser}/>,
        },
        {
            path: "/signup",
            element: user ? <Navigate to="/dashboard" /> : <SignUpForm/>,
        },


        // Can only access when logged in 
        {
            path: "/",
            element: <Layout />,
            children: [
                
                {
                    path: "/dashboard",
                    element: user ? <Dashboard user={user}/> : <Navigate to ="/" />
                },
                {
                    path: "/mypets",
                    element: user ? <PetList userId={user.id} /> : <Navigate to ="/" />
                },
                {
                    path: "/addpet",
                    element: user ? <AddPetForm userId={user.id} /> : <Navigate to ="/" />
                },
                {
                    path: "/notes",
                    element: user ? <h1>Notes</h1> : <Navigate to ="/" />
                },
                {
                    path: "/editpet/:petId",
                    element: user ? <EditPetForm user={user} /> : <Navigate to ="/" />
                },
                {
                    path: "/addactivity/:petId",
                    element: user ? <AddActivityForm user={user} /> : <Navigate to ="/" />
                },
                {
                    path: "/mystats",
                    element: user ? <Stats user={user} /> : <Navigate to ="/" />
                },
                {
                    path: "dashboard/editactivity/:activityId",
                    element: user ? <EditActivityForm user={user} /> : <Navigate to ="/" />
                },
                {
                    path: "/logout",
                    element: <Logout setUser={setUser} />
                },
                {
                    path: "*",
                    element: <NotFound/>
                }
                
            ]

        },
        {
            path: "/verify",
            element: user ? <Navigate to="/dashboard" /> : <VerifyPage />
        },

    ]

    const router = createBrowserRouter(routes)
    
    return (
        <RouterProvider router={router} />
    )
}



export default AppRouter