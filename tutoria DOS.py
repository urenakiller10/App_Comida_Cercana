def Dos(listanormal):
    if isinstance(listanormal, list):
        if listanormal == []:
            return "La lista es vacía"
        else:
            a = 0
            s = 0
            c = 0
            d = 0
            return Dos_aux(listanormal, [], [1, 0, 1, a, s, c, d, -1, 0, -1], 0, 3)
    else:
        return "El argumento debe ser una lista"
    
def Dos_aux(listanormal, listaMayor, sublista, i, j):
    if i > len(listanormal):
        return listaMayor
    elif j == 3:
        return Dos_aux(listanormal, listaMayor, listanormal[i]-1, i + 1, j + 1)
    elif j == 4:
        return Dos_aux(listanormal, listaMayor, sublista[j] = listanormal[i], i + 1, j + 1)
    elif j == 5:
        return Dos_aux(listanormal, listaMayor, sublista[j] == -listanormal[i], i + 1, j + 1)
    elif j == 6:
        return Dos_aux(listanormal, listaMayor, sublista[j] == listanormal[i]+1, i + 1, j + 1)
    elif j > 7:
        return Dos_aux(listanormal, listaMayor + [sublista], sublista, i + 1, 3)

print(Dos([6, 1, 2, 3, 8, 9, 0]))