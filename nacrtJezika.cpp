/*

1. Osnova

    Želimo, da je naš jezik sposoben zajeti letališča v državi, ter znotraj letališča vse njegove terminale gates.
    Po potrebi bi za vsako državo prikazali restavracije v njej, ter za vsako letališče pripadajoča parkirišča.

    drzava Slovenija {
        letalisce LetališčeLjubljana {
            terminal A {
                box((3,2),(5,3),(3,2),(5,3))
            }
            terminal B {
                box((3,2),(5,3),(3,2),(5,3))
            }
        }
    }

    1.1 Konstrukti
        1.1.1 
            Enota: 
                nil

        1.1.2 
            Realna števila: 
                1
                1.5

        1.1.3 
            Niz: 
                Letališče

        1.1.4 
            Koordinate: 
                (x, y)

        1.1.5 
            Bloki: 
                drzava NAZIV {
                    BLOK
                }

                letalisce NAZIV {
                    BLOK
                }

                terminali NAZIV {
                    UKAZI
                }

                restavracije NAZIV {
                    UKAZI
                }

                parkirisca NAZIV {
                    UKAZI
                }


        1.1.6 
            Ukazi:
                box ( ( x , y ) , ( x , y ) )
                point ( x , y )


2. Nadgradnje

    2.1 Elementi:

        Restavracije NAZIV {
            UKAZI
        }
        
        Parkirisca NAZIV {
            UKAZI
        }

    2.2 Povpraševanje

        Restavracije v bližini letališča
        restavracije {
            point(2,2)
            point(2,2)
            point(2,2)
            point(2,2)

            let območjeLetališča = circle((3, 4), 3);
            foreach x in območjeLetališča {
            highlight x
            }
        }

3. BNF:

    3.1 Sintaksa

        START::= DRZAVA

        DRZAVA ::= DRZAVA' DRZAVA | Ɛ
        
        DRZAVA' ::= drz NIZ { LETALISCE RESTAVRACIJA }

        RESTAVRACIJA ::= res { POINTS } | Ɛ

        LETALISCE ::= LETALISCE' LETALISCE | Ɛ

        LETALISCE' ::= let NIZ {TERMINAL PARKIRISCA}

        TERMINAL ::= TERMINAL' TERMINAL | Ɛ

        TERMINAL' ::= ter NIZ { UKAZ_TERMINAL }

        UKAZ_TERMINAL ::= box ( TOCKA TOCKA TOCKA TOCKA )

        PARKIRISCA ::= par NIZ { POINTS } |  Ɛ

        POINTS ::= POINTS' POINTS | Ɛ

        POINTS' ::= point TOCKA

        TOCKA ::= ( NUMBER , NUMBER )

        NUMBER ::= {0,...,9}+(.{0,...,9}+)?

        NIZ ::= {A,...,Z,a,...,z}+

    3.2 Terminalni simboli

        number = {0,...,9}+(.{0,...,9}+)?
        niz = {A,...,Z,a,...,z}+
        begin = {
        end = }
        vejica = ,
        lparent = (
        rparent = )
        Drzava = drz
        Restavracija = res
        Letalisce = let
        Terminal = ter
        Parkirisca = par
        Point = point
        Box = box
        skip = {\t, \n, \r, ' '}



4. Primeri

    4.1
        drz Slovenija {
            let LetališčeLjubljana {
                ter A {
                    box((3,2)(3,4)(4,4)(4,2))
                }
            }
        }
        drz Hrvaška {
            let Zagreb {
                ter A {
                    box((3,2)(3,4)(4,4)(4,2))
                }
            }
        }

    4.2
        drz Slovenija {
            let LetališčeLjubljana {
                terminal A {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                terminal B {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                terminal C {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                terminal D {
                    box((3,2)(3,4)(4,4)(4,2))
                }
            }
        }

    4.3
        drz Slovenija {
            let LetališčeLjubljana {
                terminal A {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                terminal B {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                terminal C {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                terminal D {
                    box((3,2)(3,4)(4,4)(4,2))
                }
            }
            res Restavracije {
                point(1,1)
            }
        }

    4.4
        drz Slovenija {
            let LetališčeLjubljana {
                terminal A {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                terminal B {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                terminal C {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                terminal D {
                    box((3,2)(3,4)(4,4)(4,2))
                }
            }
            res Restavracije {
                point(1,1)
                point(1,1)
                point(1,1)
                point(1,1)
                point(1,1)
            }
        }

    4.5
        drz Slovenija {
            let LetališčeLjubljana {
                terminal A {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                terminal B {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                terminal C {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                terminal D {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                parkirisca Ljubljana{
                    point(1,1)
                }
            }
            res {
                point(1,1)
                point(1,1)
                point(1,1)
                point(1,1)
                point(1,1)
            }
        }

    4.6
        drz Slovenija {
            let LetališčeLjubljana {
                terminal A {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                terminal B {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                terminal C {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                terminal D {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                parkirisca LetališčeLjubljana{
                    point(1,1)
                    point(1,1)
                    point(1,1)
                    point(1,1)
                }
            }
            res {
                point(1,1)
                point(1,1)
                point(1,1)
                point(1,1)
                point(1,1)
            }
        }
    4.7
        drz Slovenija {
            let Ljubljana {
                ter A {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                par Ljubljana{
                    point(1,1)
                    point(1,1)
                }
            }
            res {
                point(1,1)
                point(1,1)
            }
        }
    4.8
        drz Slovenija {
            let LetališčeLjubljana {
                terminal A {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                par Ljubljana{
                    point(1,1)
                    point(1,1)
                }
            }
            let LetališčeCelje {
                terminal A {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                par LetališčeCelje{
                    point(1,1)
                    point(1,1)
                }
            }
            res {
                point(1,1)
                point(1,1)
                point(1,1)
            }
        }
    4.9
        drz Slovenija {
            let LetališčeLjubljana {
                terminal A {
                    box((3,2)(3,4)(4,4)(4,2))
                }
            }
            let LetališčeCelje {
                terminal A {
                    box((3,2)(3,4)(4,4)(4,2))
                }
            }
        }
    4.10
        drz Slovenija {
            let LetališčeLjubljana {
                terminal A {
                    box((3,2)(3,4)(4,4)(4,2))
                }
                par a {
                    point(1,1)
                    point(1,1.5)
                }
            }
            let LetališčeCelje {
                terminal A {
                    box((3,2)(3,4)(4,4)(4,2))
                }
            }
            res {
                point(1,1.5)
                point(1,1)
                point(1,1)
            }
        }
*/