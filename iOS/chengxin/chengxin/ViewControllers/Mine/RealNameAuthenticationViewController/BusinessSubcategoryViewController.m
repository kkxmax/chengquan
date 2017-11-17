//
//  BusinessSubcategoryViewController.m
//  chengxin
//
//  Created by common on 4/12/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "BusinessSubcategoryViewController.h"
#import "Global.h"

@interface BusinessSubcategoryViewController ()

@end

@implementation BusinessSubcategoryViewController
{
    NSMutableArray* aryCategoryButtons;
    NSMutableArray* aryChildren;
}
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    aryCategoryButtons = [[NSMutableArray alloc] init];
    [aryCategoryButtons addObject:self.btnBusiness1];
    [aryCategoryButtons addObject:self.btnBusiness2];
    [aryCategoryButtons addObject:self.btnBusiness3];
    [aryCategoryButtons addObject:self.btnBusiness4];
    [aryCategoryButtons addObject:self.btnBusiness5];
    [aryCategoryButtons addObject:self.btnBusiness6];
    if(self.dicBusiness != nil)
    {
        aryChildren = [self.dicBusiness objectForKey:@"children"];
        for(int i = 0; i < 6; i++)
        {
            if(i < aryChildren.count)
            {
                [((UIButton*)aryCategoryButtons[i]) setTitle:[aryChildren[i] objectForKey:@"title"] forState:UIControlStateNormal] ;
                ((UIButton*)aryCategoryButtons[i]).selected = [[aryChildren[i] objectForKey:@"selected"] boolValue];
            }
            else
                ((UIButton*)aryCategoryButtons[i]).hidden = YES;
            ((UIButton*)aryCategoryButtons[i]).layer.cornerRadius = 5;
        }
        
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)onOK:(id)sender
{
    NSMutableArray* array = [[NSMutableArray alloc] init];
    for(int i = 0; i < 6; i++)
    {
        if(((UIButton*)aryCategoryButtons[i]).selected)
        {
            [array addObject:[aryChildren[i] objectForKey:@"id"]];
        }
    }
    [self.delegate businessSelected:array];
    [self dismissViewControllerAnimated:NO completion:nil];
}
-(IBAction)onBusiness:(id)sender
{
    [((UIButton*)sender) setSelected:!((UIButton*)sender).selected];
    [aryChildren[((UIButton*)sender).tag] setObject:[NSNumber numberWithBool:((UIButton*)sender).selected] forKey:@"selected"];
    
    if(((UIButton*)sender).selected)
    {
        [((UIButton*)sender) setBackgroundColor:RGB_COLOR(212, 231, 244)];
    }else
    {
        [((UIButton*)sender) setBackgroundColor:[UIColor groupTableViewBackgroundColor]];
    }
}
@end
