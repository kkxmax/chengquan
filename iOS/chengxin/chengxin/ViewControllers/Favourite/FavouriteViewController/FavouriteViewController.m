//
//  FavouriteViewController.m
//  chengxin
//
//  Created by common on 7/22/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "FavouriteViewController.h"
#import "FavouriteItemTableViewCell.h"

@interface FavouriteViewController ()

@end

@implementation FavouriteViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    //[self.favouriteItemTableView registerNib:@"FavouriteItemTableViewCell" forCellReuseIdentifier:@"FavouriteItemCell"];
}
-(void)viewWillAppear:(BOOL)animated
{
    
}
-(void)viewDidAppear:(BOOL)animated
{
    [self setUI];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma
- (void)setUI
{
    [self setTopTab:em_ChengXinLian];
    
    //self.navigationController.navigationBar.hidden = YES;
}
- (void)setTopTab:(Favourite_Tab)selection
{
    if(selection == em_WoDeGuanZhu)
    {
        if([self.chengxinlianContentsView superview] != nil)
            [self.chengxinlianContentsView removeFromSuperview];
        if([self.myFavouriteContentsView superview] != self.contentsContainer)
        {
            [self.contentsContainer addSubview:self.myFavouriteContentsView];
            [self.myFavouriteContentsView setFrame:CGRectMake(0, 0, self.contentsContainer.frame.size.width, self.contentsContainer.frame.size.height)];
        }
        self.lblMyFavouriteUnderline.hidden = NO;
        self.lblChengXinLianUnderline.hidden = YES;
        [self.btnChengXinLian setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [self.btnMyFavourite setTitleColor:[UIColor colorWithRed:246/255.0f green:184/255.0f blue:17/255.0f alpha:1]  forState:UIControlStateNormal];
    }else if(selection == em_ChengXinLian)
    {
        if([self.myFavouriteContentsView superview] != nil)
            [self.myFavouriteContentsView removeFromSuperview];
        if([self.chengxinlianContentsView superview] != self.contentsContainer)
        {
            [self.contentsContainer addSubview:self.chengxinlianContentsView];
            [self.chengxinlianContentsView setFrame:CGRectMake(0, 0, self.contentsContainer.frame.size.width, self.contentsContainer.frame.size.height)];
        }
        self.lblMyFavouriteUnderline.hidden = YES;
        self.lblChengXinLianUnderline.hidden = NO;
        [self.btnMyFavourite setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [self.btnChengXinLian setTitleColor:[UIColor colorWithRed:246/255.0f green:184/255.0f blue:17/255.0f alpha:1]  forState:UIControlStateNormal]  ;

    }
    tab_selection = selection;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(tableView == self.favouriteItemTableView)
    {
        return 220;
    }else if(tableView == self.chengxinlianItemTableView)
    {
        return 220;
    }
        return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    if(tableView == _favouriteItemTableView)
    {
        FavouriteItemTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
        if(cell == nil)
        {
            cell = (FavouriteItemTableViewCell*)[[[NSBundle mainBundle] loadNibNamed:@"FavouriteItemTableViewCell" owner:self options:nil] objectAtIndex:0];
        }
        return cell;
    }else if(tableView == _chengxinlianItemTableView)
    {
        FavouriteItemTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
        if(cell == nil)
        {
            cell = (FavouriteItemTableViewCell*)[[[NSBundle mainBundle] loadNibNamed:@"FavouriteItemTableViewCell" owner:self options:nil] objectAtIndex:0];
        }
        return cell;
    }
    
    
    return nil;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    if(tableView == self.favouriteItemTableView)
    {
        return 10;
    }else if(tableView == self.chengxinlianItemTableView)
    {
        return 10;
    }
        return 0;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}

-(IBAction)onMyFavourite:(id)sender
{
    [self setTopTab:em_WoDeGuanZhu];
}
-(IBAction)onTrustSeries:(id)sender
{
    [self setTopTab:em_ChengXinLian];
}
@end
