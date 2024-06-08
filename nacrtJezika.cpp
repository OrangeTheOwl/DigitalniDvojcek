/*

1. Osnova

    Želimo, da je naš jezik sposoben zajeti letališča v državi, ter znotraj letališča vse njegove terminale "gates".
    Po potrebi bi za vsako državo prikazali restavracije v njej, ter za vsako letališče pripadajoča parkirišča.

    drzava "Slovenija" {
        letalisce "Letališče Ljubljana" {
            terminal "A" {
                box((3,2),(5,3),(3,2),(5,3))
            }
            terminal "B" {
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
                "Letališče"

        1.1.4 
            Koordinate: 
                (x, y)

        1.1.5 
            Bloki: 
                drzava "NAZIV" {
                    BLOK
                }

                letalisce "NAZIV" {
                    BLOK
                }

                terminali "NAZIV" {
                    UKAZI
                }

                restavracije "NAZIV" {
                    UKAZI
                }

                parkirisca "NAZIV" {
                    UKAZI
                }


        1.1.6 
            Ukazi:
                box ( ( x , y ) , ( x , y ) )
                point ( x , y )


2. Nadgradnje

    2.1 Elementi:

        Restavracije "NAZIV" {
            UKAZI
        }
        
        Parkirisca "NAZIV" {
            UKAZI
        }

    2.2 Povpraševanje

        Restavracije v bližini letališča
        restavracije "Restavracije Slovenija" {
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

        DRZAVA ::= DRZAVA DRZAVA' | DRZAVA'
        
        DRZAVA' ::= drzava NIZ { LETALISCE RESTAVRACIJA }

        RESTAVRACIJA ::= restavracija NIZ { POINTS } | Ɛ

        LETALISCE ::= LETALISCE' LETALISCE | LETALISCE'

        LETALISCE' ::= letalisce NIZ {TERMINAL PARKIRISCA}

        TERMINAL ::= TERMINAL' TERMINAL | TERMINAL'

        TERMINAL' ::= terminal NIZ { UKAZ_TERMINAL }

        UKAZ_TERMINAL ::= box ( TOCKA , TOCKA, TOCKA , TOCKA )

        PARKIRISCA ::= parkirisca NIZ { POINTS } |  Ɛ

        POINTS ::= POINTS' POINTS | POINTS'

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
        Drzava = drzava
        Restavracija = restavracija
        Letalisce = letalisce
        Terminal = terminal
        Point = point
        Box = box
        skip = {\t,  \n, \r, " "}



4. Primeri

    4.1
        drzava "Slovenija" {
            letalisce "Letališče Ljubljana" {
                terminal "A" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
            }
        }

    4.2
        drzava "Slovenija" {
            letalisce "Letališče Ljubljana" {
                terminal "A" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
                terminal "B" {
                    box((4.6,2),(5,3),(3,2),(5,3))
                }
                terminal "C" {
                    box((5,2),(5,3.6),(3,2),(5,3))
                }
                terminal "D" {
                    box((6,2),(5,3),(3,2),(5,3))
                }
            }
        }

    4.3
        drzava "Slovenija" {
            letalisce "Letališče Ljubljana" {
                terminal "A" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
                terminal "B" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
                terminal "C" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
                terminal "D" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
            }
            restavracije "Restavracije Ljubljana" {
                point(1,1)
            }
        }

    4.4
        drzava "Slovenija" {
            letalisce "Letališče Ljubljana" {
                terminal "A" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
                terminal "B" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
                terminal "C" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
                terminal "D" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
            }
            restavracije "Restavracije Ljubljana" {
                point(1,1)
                point(1,1)
                point(1,1)
                point(1,1)
                point(1,1)
            }
        }

    4.5
        drzava "Slovenija" {
            letalisce "Letališče Ljubljana" {
                terminal "A" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
                terminal "B" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
                terminal "C" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
                terminal "D" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
                parkirisca{
                    point(1,1)
                }
            }
            restavracije "Restavracije Ljubljana" {
                point(1,1)
                point(1,1)
                point(1,1)
                point(1,1)
                point(1,1)
            }
        }

    4.6
        drzava "Slovenija" {
            letalisce "Letališče Ljubljana" {
                terminal "A" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
                terminal "B" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
                terminal "C" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
                terminal "D" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
                parkirisca{
                    point(1,1)
                    point(1,1)
                    point(1,1)
                    point(1,1)
                }
            }
            restavracije "Restavracije Ljubljana" {
                point(1,1)
                point(1,1)
                point(1,1)
                point(1,1)
                point(1,1)
            }
        }
    4.7
        drzava "Slovenija" {
            letalisce "Letališče Ljubljana" {
                terminal "A" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
                parkirisca{
                    point(1,1)
                    point(1,1)
                }
            }
            restavracije "Restavracije Ljubljana" {
                point(1,1)
                point(1,1)
            }
        }
    4.8
        drzava "Slovenija" {
            letalisce "Letališče Ljubljana" {
                terminal "A" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
                parkirisca{
                    point(1,1)
                    point(1,1)
                }
            }
            letalisce "Letališče Celje" {
                terminal "A" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
                parkirisca{
                    point(1,1)
                    point(1,1)
                }
            }
            restavracije "Restavracije Ljubljana" {
                point(1,1)
                point(1,1)
                point(1,1)
            }
        }
    4.9
        drzava "Slovenija" {
            letalisce "Letališče Ljubljana" {
                terminal "A" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
            }
            letalisce "Letališče Celje" {
                terminal "A" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
            }
        }
    4.10
        drzava "Slovenija" {
            letalisce "Letališče Ljubljana" {
                terminal "A" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
                parkirisca{
                    point(1,1)
                    point(1,1.5)
                }
            }
            letalisce "Letališče Celje" {
                terminal "A" {
                    box((3,2),(5,3),(3,2),(5,3))
                }
            }
            restavracije "Restavracije Ljubljana" {
                point(1,1.5)
                point(1,1)
                point(1,1)
            }
        }
*/