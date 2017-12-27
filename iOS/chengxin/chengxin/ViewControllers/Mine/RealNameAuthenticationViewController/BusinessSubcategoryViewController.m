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
    
    self.titleLable.text = self.dicBusiness[@"title"];
    if(self.dicBusiness != nil)
    {
        aryChildren = [self.dicBusiness objectForKey:@"children"];
        for(int i = 0; i < 6; i++)
        {
            if(i < aryChildren.count)
            {
                [((UIButton*)aryCategoryButtons[i]) setTitle:[aryChildren[i] objectForKey:@"title"] forState:UIControlStateNormal] ;
                [((UIButton*)aryCategoryButtons[i]) setSelected:self.isMyWatch ? [[aryChildren[i] objectForKey:@"isMyWatch"] boolValue] : [[aryChildren[i] objectForKey:@"isMyWatched"] boolValue] ];
                [((UIButton*)aryCategoryButtons[i]) setTitleColor:RGB_COLOR(51, 51, 51) forState:UIControlStateNormal] ;
                [((UIButton*)aryCategoryButtons[i]) setTitleColor:WHITE_COLOR forState:UIControlStateSelected] ;
                if(((UIButton*)aryCategoryButtons[i]).isSelected) {
                    [((UIButton*)aryCategoryButtons[i]) setBackgroundColor:RGB_COLOR(0, 122, 255)];
                }
            }
            else
                ((UIButton*)aryCategoryButtons[i]).hidden = YES;
            ((UIButton*)aryCategoryButtons[i]).layer.cornerRadius = 5;
        }
        
    }
    self.frameView.layer.cornerRadius = 5;
    self.btnOK.layer.cornerRadius = 5;
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
        if(i < aryChildren.count) {
            [aryChildren[i] setObject:[NSNumber numberWithBool:((UIButton*)aryCategoryButtons[i]).selected] forKey:self.isMyWatch ? @"isMyWatch" : @"isMyWatched"];
        }
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
//    [aryChildren[((UIButton*)sender).tag] setObject:[NSNumber numberWithBool:((UIButton*)sender).selected] forKey:self.isMyWatch ? @"isMyWatch" : @"isMyWatched"];
    
    if(((UIButton*)sender).selected)
    {
        [((UIButton*)sender) setBackgroundColor:RGB_COLOR(0, 122, 255)];
    }else
    {
        [((UIButton*)sender) setBackgroundColor:[UIColor groupTableViewBackgroundColor]];
    }
}

-(IBAction)onClose:(id)sender
{
    [self dismissViewControllerAnimated:NO completion:nil];
}

@end
