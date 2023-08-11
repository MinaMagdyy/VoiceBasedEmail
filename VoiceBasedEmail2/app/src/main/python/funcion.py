import easyimap as e

def login(username,passwordd):
    try:
        server = e.connect("imap.gmail.com",username,passwordd)
        return True
    except:
        return False

def emailed(user,password,x):
    try:
        server = e.connect("imap.gmail.com",user,password)
    except:
        return False
    i = 0
    found = False
    j = 0
    k = 1
    list = ""
    while j < 100000000:
        try:
            while server.mail(server.listids()[i]):
                email = server.mail(server.listids()[i])

                if x in email.title:
                    list = email.from_addr + " ~ " + email.body
                    return list
                
                    found = True
                    k = k + 1
                i = i + 1
        except:
            break
    if not found:
        print("no emails")

def get_Names(username,password):
    names=[]
    j=0
    i = 0
    server = e.connect("imap.gmail.com", username, password)
    while j<100000000:

        try:
            l=0
            k=0
            email = server.mail(server.listids()[i])

            while k < 10000:
                if email.from_addr[l] == "<":
                    k = 10000
                else:
                    l = l + 1

            name = email.from_addr[0:l-1]
            names.append(name + "~" + email.title)
            j = j + 1
            i=i+1
        except:
            break
    return names


