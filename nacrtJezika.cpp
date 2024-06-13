/*

1. Osnova

    Želimo, da je naš jezik sposoben zajeti letališča v državi, ter znotraj letališča vse njegove tere gates.
    Po potrebi bi za vsako državo prikazali restavracije v njej, ter za vsako letališče pripadajoča parkirišča.

    drzava Slovenija {
        letalisce LetalisceLjubljana {
            ter A {
                box((100,100)(100,105)(105,105)(105,100))
            }
            ter B {
                box((100,100)(100,105)(105,105)(105,100))
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

                teri NAZIV {
                    UKAZI
                }

                restavracije NAZIV {
                    UKAZI
                }

                par NAZIV {
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
        
        par NAZIV {
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

        LETALISCE' ::= let NIZ {ter par}

        ter ::= ter' ter | Ɛ

        ter' ::= ter NIZ { UKAZ_ter }

        UKAZ_ter ::= box ( TOCKA TOCKA TOCKA TOCKA )

        par ::= par NIZ { POINTS } |  Ɛ

        POINTS ::= POINTS' POINTS | Ɛ

        POINTS' ::= point TOCKA

        TOCKA ::= ( NUMBER , NUMBER )

        NUMBER ::= {0,...,9}+(.{0,...,9}+)?

        NIZ ::= {A,...,Z,a,...,z}+

    3.2 terni simboli

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
        ter = ter
        par = par
        Point = point
        Box = box
        skip = {\t, \n, \r, ' '}



4. Primeri

    4.1
        drz Slovenija {
            let LetalisceLjubljana {
                ter A {
                    box((100,100)(100,105)(105,105)(105,100))
                }
            }
        }
        drz Hrvaska {
            let Zagreb {
                ter A {
                    box((100,100)(100,105)(105,105)(105,100))
                }
            }
        }

    4.2
        drz Slovenija {
            let LetalisceLjubljana {
                ter A {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                ter B {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                ter C {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                ter D {
                    box((100,100)(100,105)(105,105)(105,100))
                }
            }
        }

    4.3
        drz Slovenija {
            let LetalisceLjubljana {
                ter A {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                ter B {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                ter C {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                ter D {
                    box((100,100)(100,105)(105,105)(105,100))
                }
            }
            res {
                point(1,1)
            }
        }

    4.4
        drz Slovenija {
            let LetalisceLjubljana {
                ter A {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                ter B {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                ter C {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                ter D {
                    box((100,100)(100,105)(105,105)(105,100))
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

    4.5
        drz Slovenija {
            let LetalisceLjubljana {
                ter A {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                ter B {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                ter C {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                ter D {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                par Ljubljana{
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
            let LetalisceLjubljana {
                ter A {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                ter B {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                ter C {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                ter D {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                par LetalisceLjubljana{
                    point(3,3)
                point(4,4)
                point(5,5)
                point(6,6)
                point(7,7)
                }
            }
            res {
                point(3,3)
                point(4,4)
                point(5,5)
                point(6,6)
                point(7,7)
            }
        }
    4.7
        drz Slovenija {
            let Ljubljana {
                ter A {
                    box((100,100)(100,105)(105,105)(105,100))
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
            let LetalisceLjubljana {
                ter A {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                par Ljubljana{
                    point(1,1)
                    point(1,1)
                }
            }
            let LetalisceCelje {
                ter A {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                par LetalisceCelje{
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
            let LetalisceLjubljana {
                ter A {
                    box((100,100)(100,105)(105,105)(105,100))
                }
            }
            let LetalisceCelje {
                ter A {
                    box((100,100)(100,105)(105,105)(105,100))
                }
            }
        }
    4.10
        drz Slovenija {
            let LetalisceLjubljana {
                ter A {
                    box((100,100)(100,105)(105,105)(105,100))
                }
                par a {
                    point(1,1)
                    point(1,1.5)
                }
            }
            let LetalisceCelje {
                ter A {
                    box((100,100)(100,105)(105,105)(105,100))
                }
            }
            res {
                point(1,1.5)
                point(1,1)
                point(1,1)
            }
        }
*/